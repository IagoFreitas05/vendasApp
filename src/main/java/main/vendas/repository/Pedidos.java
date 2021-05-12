package main.vendas.repository;

import main.vendas.entity.Cliente;
import main.vendas.entity.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface Pedidos  extends JpaRepository<Pedido, Integer> {
    List<Pedido> findByCliente(Cliente cliente);

    Optional<Pedido> findByIdFetchItens(Integer id);
}
