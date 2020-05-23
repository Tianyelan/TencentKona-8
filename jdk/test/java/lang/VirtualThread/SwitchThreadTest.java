/*
 * Copyright (c) 2003, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */

/*
 * @test
 * @summary Basic test for continuation, test create/run/yield/resume/stop
 */
public class SwitchThreadTest {
    static long count = 0;
    static ContinuationScope scope = new ContinuationScope("test");
    public static void main(String args[]) throws Exception {
        foo();
        System.out.println("finish first");
        foo();
        System.out.println("finish second");
        bar();
        System.out.println("finish bar 1");
        qux();
        System.out.println("finish qux 1");
        switch3threads();
        System.out.println("finish switch3threads 1");
        baz();
        System.out.println("finish baz 1");
    }

    static void foo() throws Exception {
        Runnable target = new Runnable() {
            public void run() {
                System.out.println("Continuation run in " + Thread.currentThread().getName());
                System.out.println("before yield");
                Continuation.yield(scope);
                System.out.println("Continuation run in " + Thread.currentThread().getName());
                System.out.println("resume yield");
            }
        };
        Continuation cont = new Continuation(scope, target);
        System.out.println("Continuation create in " + Thread.currentThread().getName());
        Thread thread = new Thread(){
            public void run() {
                cont.run();
                cont.run();
            }
        };
        thread.start();
        thread.join();
        //cont.run();
        //cont.run();
    }

    static void bar() throws Exception {
        Runnable target = new Runnable() {
            public void run() {
                System.out.println("Continuation run in " + Thread.currentThread().getName());
                System.out.println("before yield");
                Continuation.yield(scope);
                System.out.println("Continuation run in " + Thread.currentThread().getName());
                System.out.println("resume yield");
            }
        };
        Continuation cont = new Continuation(scope, target);
        System.out.println("Continuation create in " + Thread.currentThread().getName());
        Thread thread = new Thread(){
            public void run() {
                cont.run();
            }
        };
        thread.start();
        thread.join();
        cont.run();
        //cont.run();
    }
    // create in other thread and run in main
    static Continuation internal;
    static void qux() throws Exception {
        Runnable target = new Runnable() {
            public void run() {
                System.out.println("Continuation run in " + Thread.currentThread().getName());
                System.out.println("before yield");
                Continuation.yield(scope);
                System.out.println("Continuation run in " + Thread.currentThread().getName());
                System.out.println("resume yield");
            }
        };
        Thread thread = new Thread(){
            public void run() {
                Continuation cont = new Continuation(scope, target);
                System.out.println("Continuation create in " + Thread.currentThread().getName());
                internal = cont;
            }
        };
        thread.start();
        thread.join();
        Thread.sleep(100);
        internal.run();
        internal.run();
    }

    static void baz() throws Exception {
        Runnable target = new Runnable() {
            public void run() {
                System.out.println("Continuation run in " + Thread.currentThread().getName());
                System.out.println("before yield");
                Continuation.yield(scope);
                System.out.println("Continuation run in " + Thread.currentThread().getName());
                System.out.println("resume yield");
            }
        };
        Thread thread = new Thread(){
            public void run() {
                Continuation cont = new Continuation(scope, target);
                System.out.println("Continuation create in " + Thread.currentThread().getName());
                internal = cont;
                try {
                	Thread.sleep(2000);
                } catch (Exception e) {
                }
				cont.run();
            }
        };
        thread.start();
        Thread.sleep(1000);
        internal.run();
		thread.join();
    }


	static void switch3threads() throws Exception {
        Runnable target = new Runnable() {
            public void run() {
                System.out.println("Continuation run in " + Thread.currentThread().getName());
                System.out.println("before yield");
                Continuation.yield(scope);
                System.out.println("Continuation run in " + Thread.currentThread().getName());
                System.out.println("resume yield");
            }
        };
        Thread thread = new Thread(){
            public void run() {
                Continuation cont = new Continuation(scope, target);
                System.out.println("Continuation create in " + Thread.currentThread().getName());
                internal = cont;
            }
        };
        thread.start();
        thread.join();
        Thread thread1 = new Thread(){
            public void run() {
				internal.run();
            }
        };
        thread1.start();
        thread1.join();
        Thread thread2 = new Thread(){
            public void run() {
                internal.run();
            }
        };
        thread2.start();
        thread2.join();
    }
}