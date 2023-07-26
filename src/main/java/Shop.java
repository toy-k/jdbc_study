import java.sql.*;
import java.util.Scanner;

public class Shop {
    public static void main(String[] args) {
        System.out.println("Hello, World!\n");

        postgreSQLConnection();

        System.out.println("\nBye, World!");

    }


    public static void postgreSQLConnection() {
        String url = "jdbc:postgresql://localhost:5432/shop";
        String username = "myuser";
        String password = "1234";

        Connection connection = null;

        boolean exit = false;

        int serviceOpt = 0;

        try {

            Class.forName("org.postgresql.Driver");



            String dropProductTableQuery = "DROP TABLE IF EXISTS products";
            String dropOrderTableQuery = "DROP TABLE IF EXISTS orders";

            connection = DriverManager.getConnection(url, username, password);

            String createProductTableQuery = "CREATE TABLE IF NOT EXISTS products (" +
                    "product_id SERIAL PRIMARY KEY," +
                    "name VARCHAR(255) NOT NULL," +
                    "price INT NOT NULL," +
                    "stock_count INT NOT NULL" +
                    ")";

            String createOrderTableQuery = "CREATE TABLE IF NOT EXISTS orders (" +
                    "order_id SERIAL PRIMARY KEY," +
                    "product_id INT NOT NULL," +
                    "order_count INT NOT NULL," +
                    "FOREIGN KEY (product_id) REFERENCES products (product_id)" +
                    ")";

            Statement sqlQuery = connection.createStatement();

            sqlQuery.execute(dropOrderTableQuery);
            sqlQuery.execute(dropProductTableQuery);

            sqlQuery.execute(createProductTableQuery);
            sqlQuery.execute(createOrderTableQuery);


            String name = "sample";
            int price = 23000;
            int stockCount = 230;

            int productId = 1;
            int orderCount = 30;

            connection.setAutoCommit(false);

            String insertProductDataQuery = "INSERT INTO products (name, price, stock_count) " +
                    "VALUES (?, ?, ?)";
            PreparedStatement insertProductSqlQuery = connection.prepareStatement(insertProductDataQuery);
            insertProductSqlQuery.setString(1, name);
            insertProductSqlQuery.setInt(2, price);
            insertProductSqlQuery.setInt(3, stockCount);


            String insertOrderDataQuery = "INSERT INTO orders (product_id, order_count) " +
                    "VALUES (?, ?)";
            PreparedStatement insertOrderSqlQuery = connection.prepareStatement(insertOrderDataQuery);
            insertOrderSqlQuery.setInt(1, productId);
            insertOrderSqlQuery.setInt(2, orderCount);


            String updateProductQuery = "UPDATE products SET stock_count = stock_count - ? WHERE product_id = ?";
            PreparedStatement updateProductSqlQuery = connection.prepareStatement(updateProductQuery);
            updateProductSqlQuery.setInt(1, orderCount);
            updateProductSqlQuery.setInt(2, productId);

            int insertProductResult = insertProductSqlQuery.executeUpdate();
            int insertOrderResult = insertOrderSqlQuery.executeUpdate();
            int updateProductResult = updateProductSqlQuery.executeUpdate();

            if(insertOrderResult < 0 || insertProductResult < 0 || updateProductResult <0){
                System.out.println("sample 데이터 및 sample 주문 데이터 생성 실패했습니다.");
            }else{
                System.out.println("sample 데이터 및 sample 주문 데이터 생성 성공했습니다.");
            }

            connection.setAutoCommit(true);



            Scanner scanner = new Scanner(System.in);

            while(true){

                System.out.println("\n이용하고 싶은 서비스 번호를 선택해주세요 (1~5)");
                System.out.println("[1] 상품 추가");
                System.out.println("[2] 상품 조회");
                System.out.println("[3] 상품 주문");
                System.out.println("[4] 상품 삭제 (주문 포함)");
                System.out.println("[5] 프로그램 종료\n");

                while(true){
                    System.out.print("서비스 번호: ");
                    if(!scanner.hasNextInt()){
                        System.out.println("서비스 번호는 (1 ~ 5) 숫자만 입력 가능합니다.\n");
                        scanner.next();
                    }else{
                        serviceOpt = scanner.nextInt();
                        if(serviceOpt <1 || serviceOpt > 5){
                            System.out.println("서비스 번호는 (1 ~ 5) 숫자만 입력 가능합니다.\n");
                        }else{
                            break;
                        }
                    }
                }

                switch (serviceOpt){
                    case 1:
                        ProductInsert.insertProduct(connection);
                        break;
                    case 2:
                        ProductSearch.searchProduct(connection);
                        break;
                    case 3:
                        OrderInsertAndProductUpdate.insertOrderAndUpdateProduct(connection);
                        break;
                    case 4:
                        ProductDelete.deleteProduct(connection);
                        break;
                    case 5:
                        exit = true;
                        break;
                }

                if (exit) {
                    break;
                }
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        } finally {

            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

    }

}
