import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;

class ProductInsert {
    public static void insertProduct(Connection connection)  throws SQLException{

        try {
            Scanner scanner = new Scanner(System.in);

            String name ="";
            int price = 0;
            int stockCount = 0;


            System.out.print("상품명: ");
            name = scanner.nextLine();

            while(true){
                System.out.print("가격: ");
                if(!scanner.hasNextInt()){
                    System.out.println("가격은 (1 ~ 100000000) 숫자만 입력 가능합니다.");
                    scanner.next();
                }else{
                    price = scanner.nextInt();
                    if(price <1 || price > 100000000){
                        System.out.println("가격은 (1 ~ 100000000) 숫자만 입력 가능합니다.");
                    }else{
                        break;
                    }
                }
            }

            while(true){
                System.out.print("수량: ");
                if(!scanner.hasNextInt()){
                    System.out.println("수량은 (1 ~ 10000) 숫자만 입력 가능합니다.");
                    scanner.next();
                }else{
                    stockCount = scanner.nextInt();
                    if(stockCount <1 || stockCount > 10000){
                        System.out.println("수량은 (1 ~ 10000) 숫자만 입력 가능합니다.");
                    }else{
                        break;
                    }
                }
            }


            String insertProductQuery = "INSERT INTO products (name, price, stock_count) VALUES (?, ?, ?)";

            PreparedStatement sqlQuery = connection.prepareStatement(insertProductQuery);
            sqlQuery.setString(1, name);
            sqlQuery.setInt(2, price);
            sqlQuery.setInt(3, stockCount);

            // 상품 정보 삽입 실행
            int result = sqlQuery.executeUpdate();

            if (result > 0) {
                System.out.println("상품 정보 입력 성공했습니다");
            } else {
                System.out.println("상품 정보 입력 실패했습니");
            }

        } catch (SQLException e) {
            throw e;
        }
    }
}