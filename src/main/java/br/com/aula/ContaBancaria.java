package br.com.aula;

public class ContaBancaria{

    private double saldo;

    //Regra 1: Ao abrir a conta, o saldo não pode ser negativo
    public ContaBancaria(double saldoIncial){
        if(saldoIncial < 0){
            throw new IllegalArgumentException("O saldo inicial não pode ser negativo");
        }
        this.saldo = saldoIncial;
    }

    public double getSaldo(){
        return this.saldo;
    }

    //Regra 2: Só permite depositar valores estritamente maiores que zero
    public void depositar(double valor){
        if(valor <= 0){
            throw new IllegalArgumentException("O valor de depósito deve ser maior do que zero");
        }
        this.saldo += valor;
    }

    public void sacar(double valor){
        if(valor <=0){
            throw new IllegalArgumentException("O valor de saque deve ser maior do que zero");
        }
        if(valor > this.saldo){
            throw new IllegalArgumentException("Saldo insufuciente para realizar o saque");
        }
        this.saldo -= valor;
    }
    
}
