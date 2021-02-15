package com.pavel_chupin;

import com.pavel_chupin.db_support.ConnectDB;
import com.pavel_chupin.db_support.DBMethods;
import com.pavel_chupin.db_support.DBMethodsImpl;
import com.pavel_chupin.db_support.Data;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigInteger;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.List;

public class GenerateXML implements Serializable {
    private transient String dataBaseURL;
    private transient String dataBaseUser;
    private transient String dataBasePass;
    private long n;
    private transient DBMethods dbService;

    public GenerateXML() {

    }

    public GenerateXML(String dataBaseURL, String dataBaseUser, String dataBasePass, long n) {
        this.dataBaseURL = dataBaseURL;
        this.dataBaseUser = dataBaseUser;
        this.dataBasePass = dataBasePass;
        this.n = n;
    }

    public void setDbService(DBMethods dbService) {
        this.dbService = dbService;
    }

    public void setN(long n) {
        this.n = n;
    }

    public void setDataBaseURL(String dataBaseURL) {
        this.dataBaseURL = dataBaseURL;
    }

    public void setDataBaseUser(String dataBaseUser) {
        this.dataBaseUser = dataBaseUser;
    }

    public void setDataBasePass(String dataBasePass) {
        this.dataBasePass = dataBasePass;
    }

    public static void main(String[] args) {
        try {
            GenerateXML m = new GenerateXML(args[0], args[1], args[2], Long.parseLong(args[3]));
            m.start();
        } catch (NumberFormatException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void start() throws SQLException {
        try {
            //Инициализируем сервис
            dbService = new DBMethodsImpl(dataBaseURL, dataBaseUser, dataBasePass);
            //Инициализируем данные в базе
            //long time = System.currentTimeMillis();
            //System.out.println(time);
            dbService.initData(n);
            //System.out.println(System.currentTimeMillis() - time);

            //time = System.currentTimeMillis();
            //System.out.println(time);
            //dbService.initDataMass(n);
            //System.out.println(System.currentTimeMillis() - time);

            //Получаем список объектов в таблице
            List<Data> dataList = dbService.selectData();

            Path fileTwo = Paths.get("2.xml");
            Path fileOne = Paths.get("1.xml");
            Path fileXSL = Paths.get("xslShema.xml");

            //Сохраняем данные
            Helper.saveFile(Helper.createDocXML(dataList), fileOne);

            //Конвертим файл
            Helper.transformXML(fileXSL, fileOne, fileTwo);

            //Разбираем файл и считаем сумму
            printSum(Helper.parseDocXML(fileTwo));
        } catch (SQLException e) {
            ConnectDB.closeConnection();
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void printSum(List<Data> data) {
        BigInteger bf = BigInteger.ZERO;
        for (Data l : data) {
            bf = bf.add(BigInteger.valueOf(l.getValue()));
        }
        System.out.println(bf);
    }
}
