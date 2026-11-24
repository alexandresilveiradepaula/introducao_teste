//Define o pacote da aplicação
package br.com.aula;

//decalra uma interface qie representa o contrato de acesso ao banco de dados
public interface  ContaRepository {
    //contrato do método qie busca uma conta pelo numero identificador
    ContaBancaria buscarPorNumero(String numeroConta);
    //contrato de método que sala ou atualiza os dados de uma conta no banco. 
    void salvar(ContaBancaria conta);
    
}
