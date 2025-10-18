package dao;

import DTO.ClienteDTO;
import VIEW.Clientes;
import java.sql.*;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class ClienteDAO {

    Connection conexao = null;
    PreparedStatement pst = null;
    ResultSet rs = null;

    public void adcCliente(ClienteDTO objClienteDTO) {
    if (objClienteDTO.getNome() == null || objClienteDTO.getNome().trim().isEmpty() ||
        objClienteDTO.getTelefone() == null || objClienteDTO.getTelefone().trim().isEmpty() ||
        objClienteDTO.getEmail() == null || objClienteDTO.getEmail().trim().isEmpty()) {
        JOptionPane.showMessageDialog(null, "Preencha todos os campos.");
        return;
        }
        String sql = "INSERT INTO clientes (nome, telefone, email) VALUES (?, ?, ?)";
        conexao = DAO.ConexaoDAO.conector();

        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, objClienteDTO.getNome());
            pst.setString(2, objClienteDTO.getTelefone());
            pst.setString(3, objClienteDTO.getEmail());

            int add = pst.executeUpdate();
            if (add > 0) {
                pesquisaAuto();
                limpar();
                JOptionPane.showMessageDialog(null, "Cliente adicionado com sucesso!");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao adicionar cliente: " + e.getMessage());
        } finally {
            try {
                if (conexao != null) {
                    conexao.close();
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Erro ao fechar conexão: " + ex.getMessage());
            }
        }
    }

    public void editar(ClienteDTO objClienteDTO) {
            if (objClienteDTO.getNome() == null || objClienteDTO.getNome().trim().isEmpty() ||
        objClienteDTO.getTelefone() == null || objClienteDTO.getTelefone().trim().isEmpty() ||
        objClienteDTO.getEmail() == null || objClienteDTO.getEmail().trim().isEmpty()) {
        JOptionPane.showMessageDialog(null, "Preencha todos os campos.");
        return;
            }
        
        String sql = "UPDATE clientes SET nome = ?, telefone = ?, email = ? WHERE id_cliente = ?";
        conexao = DAO.ConexaoDAO.conector();

        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, objClienteDTO.getNome());
            pst.setString(2, objClienteDTO.getTelefone());
            pst.setString(3, objClienteDTO.getEmail());
            pst.setInt(4, objClienteDTO.getIdCliente());  // ID para WHERE

            int updated = pst.executeUpdate();
            if (updated > 0) {
                JOptionPane.showMessageDialog(null, "Cliente editado com sucesso!");
                pesquisaAuto(); 
                limpar();
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao editar cliente: " + e.getMessage());
        } finally {
            try {
                if (conexao != null) {
                    conexao.close();
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Erro ao fechar conexão: " + ex.getMessage());
            }
        }
    }

    public void limpar() {
        DefaultTableModel modelo = (DefaultTableModel) Clientes.tbClientes.getModel();
        modelo.setRowCount(0);
    }

    public void pesquisaAuto() {
        String sql = "SELECT * FROM clientes";
        conexao = DAO.ConexaoDAO.conector();
        try {
            pst = conexao.prepareStatement(sql);
            rs = pst.executeQuery();
            DefaultTableModel model = (DefaultTableModel) Clientes.tbClientes.getModel();
            model.setNumRows(0);
            while (rs.next()) {
                int id = rs.getInt("id_cliente");
                String nome = rs.getString("nome");
                String telefone = rs.getString("telefone"); // melhor usar String para telefone
                String email = rs.getString("email");
                model.addRow(new Object[]{id, nome, telefone, email});
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Método pesquisar " + e);
        } finally {
            try {
                if (conexao != null) {
                    conexao.close();
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Erro ao fechar conexão: " + ex.getMessage());
            }
        }
    }

    public void deletar(ClienteDTO objClienteDTO) {
        String sql = "DELETE FROM catalogo_filmes WHERE idFilme = ?";
        conexao = DAO.ConexaoDAO.conector();

        try {
            pst = conexao.prepareStatement(sql);
            pst.setInt(1, objClienteDTO.getIdCliente());
            int del = pst.executeUpdate();
            if (del > 0) {
                JOptionPane.showMessageDialog(null, "Cliente deletado com sucesso!");
                pesquisaAuto();
                limpar();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Método deletar " + e);
        } finally {

        }
    }
}
