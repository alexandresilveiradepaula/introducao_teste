//Define o pacote da aplicação
package br.com.aula;
//Declara uma interface que represenat o contrato de notificações externas
public interface  NotificacaoService {

    // Contrato do método que envia um comprovante de movimentação da conta
    void enviarComprovante(String numeroConta, double valor);

    
}
