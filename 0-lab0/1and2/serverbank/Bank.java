/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serverbank;

/**
 *
 * @author duarteduarte
 */
public interface Bank {

    int getBalance();

    boolean move(int value);
}
