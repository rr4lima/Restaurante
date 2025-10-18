package dao;

import DAO.ConexaoDAO;
import DTO.PedidoDTO;
import VIEW.Pedidos;
import java.sql.*;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class PedidoDAO {

    Connection conexao = null;
    PreparedStatement pst = null;
    ResultSet rs = null;

    public void adcPedido(PedidoDTO objPedidoDTO) {
        if (objPedidoDTO.getIdCliente() <= 0
                || objPedidoDTO.getData() == null || objPedidoDTO.getData().trim().isEmpty()
                || objPedidoDTO.getValor() <= 0) {
            JOptionPane.showMessageDialog(null, "Preencha todos os campos.");
            return;
        }

        String sql = "INSERT INTO pedidos (id_cliente, data_pedido, valor_total) VALUES (?, ?, ?)";
        try {
            conexao = new ConexaoDAO().conector();
            pst = conexao.prepareStatement(sql);
            pst.setInt(1, objPedidoDTO.getIdCliente());
            pst.setString(2, objPedidoDTO.getData());
            pst.setDouble(3, objPedidoDTO.getValor());

            int add = pst.executeUpdate();
            if (add > 0) {
                JOptionPane.showMessageDialog(null, "Pedido adicionado com sucesso!");
                pesquisaAuto();
                limpar();
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao adicionar pedido: " + e.getMessage());
        } finally {
            fecharRecursos();
        }
    }

    public void editar(PedidoDTO objPedidoDTO) {
        if (objPedidoDTO.getIdCliente() <= 0
                || objPedidoDTO.getData() == null || objPedidoDTO.getData().trim().isEmpty()
                || objPedidoDTO.getValor() <= 0) {
            JOptionPane.showMessageDialog(null, "Preencha todos os campos.");
            return;
        }

        String sql = "UPDATE pedidos SET id_cliente = ?, data_pedido = ?, valor_total = ? WHERE id_pedido = ?";
        try {
            conexao = new ConexaoDAO().conector();
            pst = conexao.prepareStatement(sql);
            pst.setInt(1, objPedidoDTO.getIdCliente());
            pst.setString(2, objPedidoDTO.getData());
            pst.setDouble(3, objPedidoDTO.getValor());
            pst.setInt(4, objPedidoDTO.getIdPedido());

            int updated = pst.executeUpdate();
            if (updated > 0) {
                JOptionPane.showMessageDialog(null, "Pedido editado com sucesso!");
                pesquisaAuto();
                limpar();
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao editar pedido: " + e.getMessage());
        } finally {
            fecharRecursos();
        }
    }

    public void deletar(PedidoDTO objPedidoDTO) {
        if (objPedidoDTO.getIdPedido() <= 0) {
            JOptionPane.showMessageDialog(null, "Selecione um pedido válido para deletar.");
            return;
        }

        String sql = "DELETE FROM pedidos WHERE id_pedido = ?";
        try {
            conexao = new ConexaoDAO().conector();
            pst = conexao.prepareStatement(sql);
            pst.setInt(1, objPedidoDTO.getIdPedido());

            int del = pst.executeUpdate();
            if (del > 0) {
                JOptionPane.showMessageDialog(null, "Pedido deletado com sucesso!");
                pesquisaAuto();
                limpar();
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao deletar pedido: " + e.getMessage());
        } finally {
            fecharRecursos();
        }
    }

    public void pesquisaAuto() {
        String sql = "SELECT * FROM pedidos";
        try {
            conexao = new ConexaoDAO().conector();
            pst = conexao.prepareStatement(sql);
            rs = pst.executeQuery();

            DefaultTableModel model = (DefaultTableModel) Pedidos.tbPedidos.getModel();
            model.setRowCount(0);

            while (rs.next()) {
                int id = rs.getInt("id_pedido");
                int idCliente = rs.getInt("id_cliente");
                String data = rs.getString("data_pedido");
                double valor = rs.getDouble("valor_total");

                model.addRow(new Object[]{id, idCliente, data, valor});
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao pesquisar pedidos: " + e.getMessage());
        } finally {
            fecharRecursos();
        }
    }

    public void limpar() {
        DefaultTableModel modelo = (DefaultTableModel) Pedidos.tbPedidos.getModel();
        modelo.setRowCount(0);
    }

    private void fecharRecursos() {
        try {
            if (rs != null) rs.close();
            if (pst != null) pst.close();
            if (conexao != null) conexao.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao fechar recursos: " + ex.getMessage());
        }
    }
}
