package com.luzi82.clover.runtime;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ScheduledThreadPoolExecutor;

import org.apache.commons.io.IOUtils;

import com.luzi82.common.NioNotifier;

import net.engio.mbassy.bus.MBassador;
import net.engio.mbassy.listener.Handler;

public class DeviceListChangeNotifier {

	private SocketAddress mSocketAddress;
	private NioNotifier pNioNotifier;
	private MBassador<Object> pBus;

	private SocketChannel mSocketChannel;
	private SelectionKey mSelectionKey;

	public void init(NioNotifier aNioNotifier, MBassador<Object> aBus, SocketAddress aSocketAddress) {
		if (aSocketAddress == null) {
			aSocketAddress = new InetSocketAddress("localhost", 5037);
		}
		mSocketAddress = aSocketAddress;
		pNioNotifier = aNioNotifier;
		pBus = aBus;
	}

	public void start() throws IOException {
		mSocketChannel = SocketChannel.open(mSocketAddress);
		mSocketChannel.configureBlocking(false);

		Selector selector = pNioNotifier.getSelector();
		mSelectionKey = mSocketChannel.register(selector,
				SelectionKey.OP_READ, onNio);

		ByteBuffer writeBuf = ByteBuffer.allocate(64);
		String cmd = "host:track-devices";
		writeBuf.put(String.format("%04x", cmd.length()).getBytes());
		writeBuf.put(cmd.getBytes());
		writeBuf.flip();

		mSocketChannel.write(writeBuf);
	}

	public void close() {
		mSelectionKey.cancel();
		mSelectionKey = null;
		IOUtils.closeQuietly(mSocketChannel);
	}

	private final ByteBuffer mReadBuf = ByteBuffer.allocate(1024);

	private final NioNotifier.Unit onNio = new NioNotifier.Unit() {
		public void onNio(SelectionKey sk) throws Throwable {
			int done = 0;
			while (true) {
				mReadBuf.clear();
				done = mSocketChannel.read(mReadBuf);
				if (done <= 0)
					break;
				mReadBuf.clear();
				pBus.post(new DeviceListChanged()).asynchronously();
			}
		}
	};

	public static class DeviceListChanged {
	}

	private static Object mainHandler;

	public static void main(String[] args) throws Throwable {
		ScheduledThreadPoolExecutor scheduledThreadPoolExecutor = new ScheduledThreadPoolExecutor(Runtime.getRuntime().availableProcessors());
		MBassador<Object> bus = new MBassador<Object>();
		NioNotifier nioNotifier = new NioNotifier();
		nioNotifier.init(scheduledThreadPoolExecutor);

		DeviceListChangeNotifier deviceListChangeNotifier = new DeviceListChangeNotifier();
		deviceListChangeNotifier.init(nioNotifier, bus, null);

		mainHandler = new Object() {
			@Handler
			public void handle(DeviceListChanged dlc) {
				System.out.println("BLHBXHLS DeviceListChanged " + System.currentTimeMillis());
			}
		};

		bus.subscribe(mainHandler);

		nioNotifier.start();

		deviceListChangeNotifier.start();
	}
	
}
