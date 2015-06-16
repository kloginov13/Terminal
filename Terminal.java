/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package terminal;

import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 *
 * Консоль. В операционной системе, бинарный семафор используется для
 * контролирования доступа к консоли. Консоль используется пользователями и
 * системным процессом. Построить модель системы в которой каждый пользователь и
 * системный процесс представлены отдельными потоками.
 */
public class Terminal {

    static Semaphore lock = new Semaphore(1);  // бинарный семафор контроля доступа

    /* Класс, описывающий пользователя */
    private static class User implements Runnable {

        public int id;

        public User(int id) {
            this.id = id;
        }

        public void useTerminal() {
            try {
                lock.acquire(); // пытаемся захватить терминал
            } catch (InterruptedException ex) {
                Logger.getLogger(Terminal.class.getName()).log(Level.SEVERE, null, ex);
            }
            System.out.println("User " + id + " use terminal"); //захватили терминал, оповестим
            try {
                Thread.sleep(1000); // используем терминал
            } catch (InterruptedException ex) {
                Logger.getLogger(Terminal.class.getName()).log(Level.SEVERE, null, ex);
            }
            System.out.println("User " + id + " release terminal");
            lock.release();
        }

        @Override
        public void run() {
            while (true) {
                useTerminal(); // пытаемся захватить терминал
                try {
                    Thread.sleep(5000); // отдыхаем
                } catch (InterruptedException ex) {
                    Logger.getLogger(Terminal.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
    /* Класс, описывающий системный процессы */

    private static class SystemProcess implements Runnable {

        public void useTerminal() {
            try {
                lock.acquire(); // пытаемся захватить терминал
            } catch (InterruptedException ex) {
                Logger.getLogger(Terminal.class.getName()).log(Level.SEVERE, null, ex);
            }
            System.out.println("SystemProcess use terminal"); //захватили терминал, оповестим
            try {
                Thread.sleep(1000); // используем терминал
            } catch (InterruptedException ex) {
                Logger.getLogger(Terminal.class.getName()).log(Level.SEVERE, null, ex);
            }
            System.out.println("SystemProcess release terminal");
            lock.release();
        }

        @Override
        public void run() {
            while (true) {
                useTerminal(); // пытаемся захватить терминал
                try {
                    Thread.sleep(5000); // отдыхаем
                } catch (InterruptedException ex) {
                    Logger.getLogger(Terminal.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
    public static void main(String[] args) {
        Thread sys = new Thread(new SystemProcess(), "systemProcess");
        sys.start();
        for (int i = 0; i < 10; i++) {
            
            new Thread(new User(i), "user" + i).start();
        }
    }
}
//==============================================================================
