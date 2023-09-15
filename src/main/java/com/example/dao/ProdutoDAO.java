package com.example.dao;

import com.example.entity.Produto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProdutoDAO implements DAO<Produto>{

    @Override
    public Produto get(Long id) {
        Produto produtos = null;
        String sql = "select * from produtos where id = ?";

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
                produtos = new Produto(rset.getString("marca"), rset.getString("descricao"), rset.getString("ml"), rset.getDouble("preco"),  rset.getString("link_imagem"));

                produtos.setId(rset.getLong("id"));
            }
        } catch (Exception e){
            e.printStackTrace();
        } finally {
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

        return produtos;
    }

    @Override
    public int save(Produto produtos) {
        String sql = "insert into produtos (marca, descricao, ml, preco, link_imagem)" + " values (?, ?, ?, ?, ?)";

        //Recupera a conexao com o banco
        Connection conexao = null;

        //criar preparacao da consulta
        PreparedStatement stm = null;

        try {
            conexao = ConnectionDb.getConnection();

            stm = conexao.prepareStatement(sql);
            stm.setString(1, produtos.getMarca());
            stm.setString(2, produtos.getDescricao());
            stm.setString(3, produtos.getMl());
            stm.setDouble(4, produtos.getPreco());
            stm.setString(5, produtos.getLinkImagem());

            stm.execute();

        } catch (Exception e){
            e.printStackTrace();
        } finally {
            try {
                if (stm != null){
                    stm.close();
                }
                if (conexao != null){
                    conexao.close();
                }
                return 1;
            } catch (Exception e){
                e.printStackTrace();
            }
        }
        return 0;
    }

    @Override
    public boolean update(Produto produtos, String[] params) {
        String sql = "update produtoss set marca = ?, set descricao = ?, set ml = ?, set preco = ?, set link_imagem = ? where id = ?";

        //Recupera a conexao com o banco
        Connection conexao = null;

        //criar preparacao da consulta
        PreparedStatement stm = null;

        try {
            conexao = ConnectionDb.getConnection();

            stm = conexao.prepareStatement(sql);
            stm.setString(1, produtos.getMarca());
            stm.setString(2, produtos.getDescricao());
            stm.setString(3, produtos.getMl());
            stm.setDouble(4, produtos.getPreco());
            stm.setString(5, produtos.getLinkImagem());
            stm.setLong(6, produtos.getId());

            stm.execute();

        } catch (Exception e){
            e.printStackTrace();
        } finally {
            try {
                if (stm != null){
                    stm.close();
                }
                if (conexao != null){
                    conexao.close();
                }
                return true;
            } catch (Exception e){
                e.printStackTrace();
            }
        }
        return false;
    }

    @Override
    public boolean delete(Produto produtos) {
        String sql = "delete from produtos where id = ?";

        //Recupera a conexao com o banco
        Connection conexao = null;

        //criar preparacao da consulta
        PreparedStatement stm = null;

        try {
            conexao = ConnectionDb.getConnection();

            stm = conexao.prepareStatement(sql);
            stm.setLong(1, produtos.getId());

            stm.execute();

        } catch (Exception e){
            e.printStackTrace();
        } finally {
            try {
                if (stm != null){
                    stm.close();
                }
                if (conexao != null){
                    conexao.close();
                }
                return true;
            } catch (Exception e){
                e.printStackTrace();
            }
        }
        return false;
    }

    public List<Produto> getAllProdutos() {
        List<Produto> productList = new ArrayList<>();
        String allProducts = "SELECT * FROM produtos";
        try (Connection connection = ConnectionDb.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(allProducts);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                long id = resultSet.getInt("id");
                String marca = resultSet.getString("marca");
                String descricao = resultSet.getString("descricao");
                String ml = resultSet.getString("ml");
                Double preco = resultSet.getDouble("preco");
                String linkImagem = resultSet.getString("link_imagem");

                Produto product = new Produto(marca, descricao, ml, preco, linkImagem);
                product.setId(id);
                productList.add(product);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return productList;
    }
}
