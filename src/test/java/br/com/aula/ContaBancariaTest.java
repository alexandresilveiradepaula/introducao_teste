package br.com.aula;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.beans.Transient;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import main.java.br.com.aula.ContaBancaria;

 class ContaBancariaTest {

    private ContaBancaria conta;

    //Executado automaticamente antes de cada método @Test
    //Garante que todo teste começe com uma conta nova e limpa contendo 100,00 reais

    @BeforeEach
    void  setup(){
        this.conta = new ContaBancaria(100.0);
    }

    //TESTE 1: Caminho Feliz(Depósito)
    @Test
    @DisplayName("deve somar o valor ao saldo ao realizar um depósito válido")
    void depositarValorValidarcomSucesso(){
        //1. Arrange(Preparar)
        double valorDeposito = 50.0;

        //2.Act(agir)
        conta.depositar(valorDeposito);

        //3. Assert (Verificar)
        assertEquals(150.0, conta.getSaldo());
    }

    //TEST 2: Caminho Feliz (saque)
    @Test
    @DisplayName("Deve subtrair o valor do saldo ao realizar um saque válido")
        void deveSacarValorvalidoComSucesso(){
            //1. Preparar
            double valorSaque = 30.0;

            //2. Agir
            conta.sacar(valorSaque);

            //3. Verficar
            assertEquals(70.0, conta.getSaldo());
        }

        // TESTE 3: Caminho Triste/Exceção()
        @Test
        @DisplayName
        void deveLancarExcecaoAoDepositarvalorInvalido(){
            assertThrows(IllegalArgumentException.class,()->{
                conta.depositar(-10.0);
            });
        }

        //TESTE 4: Caminho Triste / Exceção (Saldo Insuficiente)
        @Test
        @DisplayName
        void deveLancarExcecaoAoSacarValorMaiorQueSaldo(){
            assertThrows(IllegalArgumentException.class,()->{
                conta.sacar(200.0);
            });
        }
    
}
