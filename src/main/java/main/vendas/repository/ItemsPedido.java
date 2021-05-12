package main.vendas.repository;

import main.vendas.entity.ItemPedido;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemsPedido  extends JpaRepository<ItemPedido, Integer> {
}
