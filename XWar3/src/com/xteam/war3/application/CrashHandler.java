package com.xteam.war3.application;

import java.lang.Thread.UncaughtExceptionHandler;

public class CrashHandler implements UncaughtExceptionHandler {

	@Override
	public void uncaughtException(Thread thread, Throwable ex) {
		ex.printStackTrace();
		System.exit(0);
	}

}
