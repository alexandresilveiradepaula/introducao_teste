//Declaração do Pacote
package br.com.aula;

// Importa o assertEquals para verificar os saldos finais da conta
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import  org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;


//Ativa o motor do Mockito antes de executar os testes dessa classe
@ExtendWith(MockitoExtension.class)
//Declara a classe de teste de TransferenciaService
class TransferenciaServiceTest{

    //Cria um dublê falso(Mock) de ContaRepository sem banco de dados real
    @Mock 
    private ContaRepository contaRepository;

    //Cria um dublê falso(Mock) de NotificacaoService sem envio real de mensagens
    @Mock 
    private NotificacaoService notificacaoService;

    //Cria a instancia REAL de TransferenciaService injetando os dois mocks acima criados
    @InjectMocks 
    private TransferenciaService transferenciaService;

    //Marca o método  de teste unitário 
    @Test
    //Descreve o nome do teste
    @DisplayName("Deve transferir valor, saldas as duas contas e enviar o comprovante")
    //testa o fluxo principal quando as duas contas existem e tem saldo
    void deveRealizarTransferenciaComSucesso(){

        //Preparar: Cria duas contas e coloco saldo nelas
        ContaBancaria contaOrigem = new ContaBancaria(500.0);
        ContaBancaria contaDestino = new ContaBancaria(100.0);

        //programa o mock: quando buscar "111" deve devolver conta origem
        when(contaRepository.buscarPorNumero("111")).thenReturn(contaOrigem);
        // programa o mock: quando buscar "222" deve devolver conta destino
        when(contaRepository.buscarPorNumero("222")).thenReturn(contaDestino);

        //Agir: Executa o método real de transferencia com 200.00
        transferenciaService.transferir("111","222",200.0);

        //Verificar: Verificar se o saldo da origem caiu de 500 para 300
        assertEquals(300.0,contaOrigem.getSaldo());
        //Verificar: Verifica se salda da conta destino saiu de 100 para 300
        assertEquals(300.0,contaDestino.getSaldo());

        //verificar(Comportamento): Confirma que salvar(contaOrigem) foi acionado somente 1 vez
        verify(contaRepository, times(1)).salvar(contaOrigem);
        //verificar(Comportamento): Confirma que salvar(contaDestino) foi acionado somente 1 vez
        verify(contaRepository, times(1)).salvar(contaDestino);
        //Verificar(Comportamento): Confiram que o comprovante foi enviado 1 vez com os dados certos
        verify(notificacaoService, times(1)).enviarComprovante("111",200.0);

    }
    //Marca um método como teste
    @Test
    //Descreve o teste a ser realizado
    @DisplayName("dve lançar exceção e não salvar quando a conta de origem não existir")
    //Testa se o servico aborta a operação para retornar null ao buscar conta 999
    void deveLancarExcecaoQuandoContaOrigemNaoexistir(){
        // Programa o mock para retornar null ao pesquisar a conta 999 
        when(contaRepository.buscarPorNumero("999")).thenReturn(null);
        // Verifica se IlegalArgumentException é lançada ao transferir
        assertThrows(IllegalArgumentException.class,()->{
            //testa transferir apartir de conta inexistente
            transferenciaService.transferir("999","222",100.0);
        });
        // Garante que o método salvar nunca foi chamado
        verify(contaRepository, never()).salvar(any()); 
        // Garante que nenhuma notificação foi enviada
        verifyNoInteractions(notificacaoService);
    }


    @Test 
    @DisplayName("Deve interromper a operação e nçao notificar se o saldo for insuficiente")
    // Testa se o fluxo para no meio da operação caso saldo insuficiente
    void deveLancarExcecaoQuandoSaldoForInsuficiente(){
        
        // Cria as contas e lança saldo nelas
        ContaBancaria contaOrigem = new ContaBancaria(50.0);
        ContaBancaria contaDestino = new ContaBancaria(100.0);
           //programa o mock: quando buscar "111" deve devolver conta origem
        when(contaRepository.buscarPorNumero("111")).thenReturn(contaOrigem);
         // programa o mock: quando buscar "222" deve devolver conta destino
        when(contaRepository.buscarPorNumero("222")).thenReturn(contaDestino);

        // verifica se ao lançar um saque de 200 em uma conta com 50 de saldo, uma exceção é lançada
        assertThrows(IllegalArgumentException.class,()-> {
            // Executa uma tentativa de saque
            transferenciaService.transferir("111","222",200.0);

        });
        // Garante  que nenhuma conta foi salva após o erro
        verify(contaRepository, never()).salvar(any());
        // Garante que o cliente não recebu comprovant falso 
        verifyNoInteractions(notificacaoService);
        
    }
}






  

