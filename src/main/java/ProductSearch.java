import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class ProductSearch {

    public static void searchProduct(Connection connection) throws SQLException{
        try {
            Scanner scanner = new Scanner(System.in);

            System.out.print("상품명 검색: ");

            String searchProductName = scanner.nextLine();

            String searchProductQuery = "SELECT * FROM products WHERE name = ?";
            PreparedStatement sqlQuery = connection.prepareStatement(searchProductQuery);
            sqlQuery.setString(1, searchProductName);
            //검색엔진과 같은 상황이라면 아래와 같이 셋팅가능할듯
            //SELECT * FROM products WHERE name LIKE ?
            // sqlQuery.setString(1, "%" + searchProductName + "%")

            ResultSet result = sqlQuery.executeQuery();

            if (result.next()) {
                System.out.println("검색 결과:");
                do {
                    int productId = result.getInt("product_id");
                    String name = result.getString("name");
                    int price = result.getInt("price");
                    int stockCount = result.getInt("stock_count");

                    System.out.println("Product ID: " + productId);
                    System.out.println("Name: " + name);
                    System.out.println("Price: " + price);
                    System.out.println("Stock Count: " + stockCount);
                    System.out.println();
                } while (result.next());
            } else {
                System.out.println("검색 결과가 없습니다.");
            }
        }catch(SQLException e){
            throw e;
        }
    }
}
