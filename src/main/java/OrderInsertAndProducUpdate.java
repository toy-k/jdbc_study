import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

class OrderInsertAndProductUpdate {
    public static void insertOrderAndUpdateProduct(Connection connection)  throws SQLException{
        try {
            Scanner scanner = new Scanner(System.in);

            int productId=0;

            while(true){
                System.out.print("수정 할 상품 코드: ");
                if(!scanner.hasNextInt()){
                    System.out.println("수정 할 상품 코드는 (1 ~ 100000000) 숫자만 입력 가능합니다.");
                    scanner.next();
                }else{
                    productId = scanner.nextInt();
                    if(productId <1 || productId > 100000000){
                        System.out.println("수정 할 상품 코드는 (1 ~ 100000000) 숫자만 입력 가능합니다.");
                    }else{
                        break;
                    }
                }
            }

            connection.setAutoCommit(false);

            String searchProductQuery = "SELECT * FROM products WHERE product_id = ?";
            PreparedStatement searchProductSqlQuery = connection.prepareStatement(searchProductQuery);
            searchProductSqlQuery.setInt(1, productId);
            ResultSet searchProductResult = searchProductSqlQuery.executeQuery();

            boolean productFound = searchProductResult.next();

            if (!productFound) {
                System.out.println("상품이 검색되지 않았습니다.");
                return;
            }

            int availableStock = searchProductResult.getInt("stock_count");



            int orderCount = 0;
            while(true){
                System.out.print("주문 수량: ");
                if(!scanner.hasNextInt()){
                    System.out.println("주문 수량은  (1 ~ 10000) 숫자만 입력 가능합니다.");
                    scanner.next();
                }else{
                    orderCount = scanner.nextInt();
                    if(orderCount <1 || orderCount > 10000){
                        System.out.println("주문 수량은  (1 ~ 10000) 숫자만 입력 가능합니다.");
                    } else if (orderCount > availableStock) {
                        System.out.println("주문 수량이 재고 수량을 초과합니다. 재고는 " + availableStock + "개 남아있습니다.");
                    } else {
                        break;
                    }
                }
            }


            String insertOrderQuery = "INSERT INTO orders (product_id, order_count) VALUES (?, ?)";

            PreparedStatement insertOrderSqlQuery = connection.prepareStatement(insertOrderQuery);
            insertOrderSqlQuery.setInt(1, productId);
            insertOrderSqlQuery.setInt(2, orderCount);
            int insertOrderResult = insertOrderSqlQuery.executeUpdate();

            if (insertOrderResult > 0) {
//                System.out.println("주문서 생성 성공");
            } else {
                System.out.println("주문서 생성 실패");
                connection.rollback();
                return;
            }



            String updateProductQuery = "UPDATE products SET stock_count = stock_count - ? WHERE product_id = ?";

            PreparedStatement updateProductSqlQuery = connection.prepareStatement(updateProductQuery);
            updateProductSqlQuery.setInt(1, orderCount);
            updateProductSqlQuery.setInt(2, productId);
            int updateProductResult = updateProductSqlQuery.executeUpdate();

            if (updateProductResult > 0) {
                System.out.println("주문 및 상품 업데이트 성공");
                connection.commit();
            } else {
                System.out.println("주문 또는 상품 업데이트 실패");
                connection.rollback();
            }

            // 트랜잭션 종료
            connection.setAutoCommit(true);

        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                throw ex;
            }
        }
    }
}