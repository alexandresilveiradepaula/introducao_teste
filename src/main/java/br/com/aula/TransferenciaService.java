//Declaração do pacote da aplicação
package br.com.aula;

//declara a classe do serviço de transferencia entre duas contas
public class TransferenciaService {

    //Declara a dependencia imutavel do repositório de contas
    private final ContaRepository contaRepository;

    //declara a dependencia imutavel do serviço de notificações 
    private final NotificacaoService notificacaoService;

    //construtor que recebe as dependencias exterrnas(injeção de dependencias)
    public TransferenciaService(ContaRepository contaRepository, NotificacaoService notificacaoService){
        //Armazena a referencia do repository recebido
        this.contaRepository = contaRepository;
        //Armazena a referencia do serrviço de notificações recebido
                this.notificacaoService = notificacaoService;
    }
    // Método principal de negócio que executa a transferncia entre contas
    public void transferir(String numeroOrigem, String numeroDestino, double valor){
        //Consulta a conta de origem no repositório pelo numero informado
        ContaBancaria origem = contaRepository.buscarPorNumero(numeroOrigem);
        //verifica se a conta de origem não foi encontrada(retornou null)
        if(origem == null){
            //Interrompe a operação informando que a conta de origem não existe
            throw new IllegalArgumentException("Conta de origem não encontrada");
        }
        //Consulta a conta de destino no repositório de contas pelo numero informado
        ContaBancaria destino = contaRepository.buscarPorNumero(numeroDestino);
        //Verifica se a conta de destino não foi encontrada
        if(destino == null){
            //interrompe a operação indformando que a conta de destino não foi encontrada.
            throw  new IllegalArgumentException("Conta de destino não encontrada");
        }
        // saca o valor da conta de origem 
        origem.sacar(valor);
        // deposita o valor na conta de destino
        destino.depositar(valor);

        //persiste o novo saldo da conta de origem no repositório
        contaRepository.salvar(origem);
        // persiste o novo salda da conta de destino no repositório 
        contaRepository.salvar(destino);    
        //Dispara o envio de notificação para o titular da conta de origem
        notificacaoService.enviarComprovante(numeroOrigem, valor);
    }
    
}
