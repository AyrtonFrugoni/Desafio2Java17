package application;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;
import java.util.stream.Collectors;

import entities.Sale;

public class Program {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Entre o caminho do arquivo: ");
        String filePath = scanner.nextLine();
System.out.println();
        try {
            List<Sale> sales = readSalesData(filePath);

            System.out.println("Total de vendas por vendedor:");
            Map<String, Double> totalSalesBySeller = calculateTotalSalesBySeller(sales);

            totalSalesBySeller.forEach((seller, total) -> System.out.printf("%s - R$ %.2f%n", seller, total));
        } catch (IOException e) {
            System.out.println("Erro: " + e.getMessage());
        }

        scanner.close();
    }

    private static List<Sale> readSalesData(String filePath) throws IOException {
        List<Sale> sales = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new FileReader(filePath));

        sales = reader.lines()
                .map(line -> {
                    String[] fields = line.split(",");
                    if (fields.length == 5) {
                        int month = Integer.parseInt(fields[0]);
                        int year = Integer.parseInt(fields[1]);
                        String seller = fields[2];
                        int items = Integer.parseInt(fields[3]);
                        double total = Double.parseDouble(fields[4]);
                        return new Sale(month, year, seller, items, total);
                    }
                    return null;
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        reader.close();
        return sales;
    }

    private static Map<String, Double> calculateTotalSalesBySeller(List<Sale> sales) {
        return sales.stream()
                .collect(Collectors.groupingBy(Sale::getSeller, Collectors.summingDouble(Sale::getTotal)));
    }
}
