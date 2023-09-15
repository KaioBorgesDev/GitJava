package com.example.dao;

import com.example.components.Notifier;
import com.example.entity.Cliente;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ClienteDAO implements DAO<Cliente>{

    @Override
    public Cliente get(Long id) {
        Cliente clientes = null;
        String sql = "select * from clientes where id = ?";

        //Recupera a conexao com o banco
        Connection conexao = null;

        //criar preparacao da consulta
        PreparedStatement stm = null;

        //Criar uma classe que guarde o retorno da operacao
        ResultSet rset;

        try {

            conexao = ConnectionDb.getConnection();

            stm = conexao.prepareStatement(sql);
            stm.setInt(1, id.intValue());
            rset = stm.executeQuery();

            while (rset.next()){
                clientes = new Cliente(rset.getString("nome_de_usuario"), rset.getString("email"), rset.getString("senha"));

                clientes.setId(rset.getLong("id"));
                clientes.setCode(rset.getString("codigo"));
                clientes.setAdm(rset.getInt("is_adm"));
            }
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            closeResources(stm, conexao);
        }

        return clientes;
    }

    @Override
    public int save(Cliente clientes) {
        String sql = "insert into clientes (nome_de_usuario, email, senha)" + " values (?, ?, ?)";

        //Recupera a conexao com o banco
        Connection conexao = null;

        //criar preparacao da consulta
        PreparedStatement stm = null;

        try {
            conexao = ConnectionDb.getConnection();

            stm = conexao.prepareStatement(sql);

            stm.setString(1, clientes.getNomeDeUsuario());
            stm.setString(2, clientes.getEmail());
            stm.setString(3, clientes.getSenha());

            stm.execute();
        } catch (Exception e){
            e.printStackTrace();
            return 0;
        } finally {
            closeResources(stm, conexao);
            return 1;
        }
    }

    @Override
    public boolean update(Cliente clientes, String[] params) {
        String sql = "update clientes set nome_de_usuario = ?, set email = ?, set senha = ?, set codigo = ?, set is_adm = ? where id = ?";

        //Recupera a conexao com o banco
        Connection conexao = null;

        //criar preparacao da consulta
        PreparedStatement stm = null;

        try {
            conexao = ConnectionDb.getConnection();

            stm = conexao.prepareStatement(sql);

            stm.setString(1, clientes.getNomeDeUsuario());
            stm.setString(2, clientes.getEmail());
            stm.setString(3, clientes.getSenha());
            stm.setString(4, clientes.getCode());
            stm.setInt(5, clientes.isAdm());
            stm.setLong(6, clientes.getId());

            stm.execute();

        } catch (Exception e){
            e.printStackTrace();
        } finally {
            closeResources(stm, conexao);
        }
        return false;
    }

    @Override
    public boolean delete(Cliente clientes) {
        String sql = "delete from clientes where id = ?";

        //Recupera a conexao com o banco
        Connection conexao = null;

        //criar preparacao da consulta
        PreparedStatement stm = null;

        try {
            conexao = ConnectionDb.getConnection();

            stm = conexao.prepareStatement(sql);
            stm.setLong(1, clientes.getId());

            stm.execute();

        } catch (Exception e){
            e.printStackTrace();
        } finally {
            closeResources(stm, conexao);
        }
        return false;
    }

    public boolean verificarLogin(String email, String senha) {
        Connection conexao = null;
        PreparedStatement stmt = null;
        ResultSet resultSet = null;

        try {
            conexao = ConnectionDb.getConnection();

            String sql = "SELECT COUNT(*) FROM clientes WHERE email = ? AND senha = ?";
            stmt = conexao.prepareStatement(sql);
            stmt.setString(1, email);
            stmt.setString(2, senha);

            resultSet = stmt.executeQuery();

            if (resultSet.next() && resultSet.getInt(1) > 0) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(resultSet, stmt, conexao);
        }

        return false;
    }

    public boolean verificarNomeDeUsuarioExistente(String nomeDeUsuario) {
        String sql = "SELECT COUNT(*) FROM clientes WHERE nome_de_usuario = ?";
        Connection conexao = null;
        PreparedStatement stmt = null;
        ResultSet resultSet = null;

        try {
            conexao = ConnectionDb.getConnection();
            stmt = conexao.prepareStatement(sql);
            stmt.setString(1, nomeDeUsuario);
            resultSet = stmt.executeQuery();

            if (resultSet.next() && resultSet.getInt(1) > 0) {
                return true; // Nome de usuário já existe
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(resultSet, stmt, conexao);
        }
        return false; // Nome de usuário não existe
    }

    public boolean verificarEmailExistente(String email) {
        String sql = "SELECT COUNT(*) FROM clientes WHERE email = ?";
        Connection conexao = null;
        PreparedStatement stmt = null;
        ResultSet resultSet = null;

        try {
            conexao = ConnectionDb.getConnection();
            stmt = conexao.prepareStatement(sql);
            stmt.setString(1, email);
            resultSet = stmt.executeQuery();

            if (resultSet.next() && resultSet.getInt(1) > 0) {
                return true; // Email já existe
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(resultSet, stmt, conexao);
        }
        return false; // Email não existe
    }

    private void closeResources(ResultSet resultSet, PreparedStatement stmt,  Connection conexao){
        if (resultSet != null) {
            try {
                resultSet.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (conexao != null) {
            try {
                conexao.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void closeResources(PreparedStatement stm,  Connection conexao){
        try {
            if (stm != null){
                stm.close();
            }
            if (conexao != null){
                conexao.close();
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
