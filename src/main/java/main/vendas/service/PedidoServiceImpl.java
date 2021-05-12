package main.vendas.service;

import lombok.RequiredArgsConstructor;
import main.vendas.entity.Cliente;
import main.vendas.entity.ItemPedido;
import main.vendas.entity.Pedido;
import main.vendas.entity.Produto;
import main.vendas.exception.RegraNegocioException;
import main.vendas.repository.Clientes;
import main.vendas.repository.ItemsPedido;
import main.vendas.repository.Pedidos;
import main.vendas.repository.Produtos;
import main.vendas.rest.dto.ItemPedidoDTO;
import main.vendas.rest.dto.PedidoDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PedidoServiceImpl implements  PedidoService {

    private final Pedidos repository;
    private final Clientes clientesRepository;
    private final Produtos produtosRepository;
    private final ItemsPedido itemsPedidoRepository;

    @Override
    @Transactional
    public Pedido salvar(PedidoDTO dto) {
        Integer id_cliente = dto.getCliente();
        Cliente cliente =  clientesRepository.findById(id_cliente).orElseThrow( () -> new RegraNegocioException("código de cliente invalido"));
        Pedido pedido = new Pedido();
        pedido.setTotal(dto.getTotal());
        pedido.setDataPedido(LocalDate.now());
        pedido.setCliente(cliente);
        List<ItemPedido> itemsPedido = converterItems(pedido, dto.getItems());
        repository.save(pedido);
        itemsPedidoRepository.saveAll(itemsPedido);
        pedido.setItens(itemsPedido);

        return pedido;
    }

    @Override
    public Optional<Pedido> obterPedidoCompleto(Integer id) {
        return repository.findByIdFetchItens(id);
    }

    private List<ItemPedido> converterItems(Pedido pedido,List<ItemPedidoDTO> items){
        if(items.isEmpty()){
            throw  new RegraNegocioException("Não é possível realizar o pedido sem items");
        }
        return items
                .stream()
                .map(dto ->{
            Integer idProduto = dto.getProduto();
            Produto produto = produtosRepository.findById(idProduto).orElseThrow(()-> new RegraNegocioException("código de produto inválido:"+idProduto));
            ItemPedido itemPedido = new ItemPedido();
            itemPedido.setPedido(pedido);
            itemPedido.setQuantidade(dto.getQuantidade());
            itemPedido.setProduto(produto);
            return itemPedido;
        }).collect(Collectors.toList());
    }
}
