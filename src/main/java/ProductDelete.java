import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;

class ProductDelete {
    public static void deleteProduct(Connection connection)  throws SQLException{
        try {
            Scanner scanner = new Scanner(System.in);

            int productId=0;

            while(true){
                System.out.print("삭제할 상품 코드: ");
                if(!scanner.hasNextInt()){
                    System.out.println("삭제할 상품 코드는 (1 ~ 100000000) 숫자만 입력 가능합니다.");
                    scanner.next();
                }else{
                    productId = scanner.nextInt();
                    if(productId <1 || productId > 100000000){
                        System.out.println("삭제할 상품 코드는 (1 ~ 100000000) 숫자만 입력 가능합니다.");
                    }else{
                        break;
                    }
                }
            }

            String deleteOrdersQuery = "DELETE FROM orders WHERE product_id = ?";
            PreparedStatement orderSqlQuery = connection.prepareStatement(deleteOrdersQuery);
            orderSqlQuery.setInt(1, productId);
            orderSqlQuery.executeUpdate();

            String deleteProductQuery = "DELETE FROM products WHERE product_id = ?";
            PreparedStatement productSqlQuery = connection.prepareStatement(deleteProductQuery);
            productSqlQuery.setInt(1, productId);

            int result = productSqlQuery.executeUpdate();

            if (result > 0) {
                System.out.println("상품 삭제 성공했습니다.");
            } else {
                System.out.println("해당 상품 코드를 가진 상품이 존재하지 않습니다.");
            }

        } catch (SQLException e) {
            throw e;
        }
    }
}