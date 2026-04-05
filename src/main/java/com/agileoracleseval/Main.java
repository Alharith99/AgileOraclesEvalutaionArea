package com.agileoracleseval;

import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[] args) throws IOException {

        String filePath = "src/main/java/com/agileoracleseval/board.txt";
        String statePath = "src/main/java/com/agileoracleseval/snakeState.txt";

        // Read grid
        List<String> lines = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                lines.add(line.replace(" ", ""));
            }
        }

        int rows = lines.size();
        int cols = lines.get(0).length();

        char[][] grid = new char[rows][cols];
        for (int r = 0; r < rows; r++) {
            grid[r] = lines.get(r).toCharArray();
        }

        List<int[]> snakeList = new LinkedList<>();

        File stateFile = new File(statePath);

        if (stateFile.exists()) {
            try (BufferedReader br = new BufferedReader(new FileReader(stateFile))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] p = line.split(",");
                    int r = Integer.parseInt(p[0]);
                    int c = Integer.parseInt(p[1]);
                    snakeList.add(new int[]{r, c});
                }
            }

            // clear grid and rebuild snake
            for (int r = 0; r < rows; r++) {
                Arrays.fill(grid[r], '-');
            }
            for (int[] part : snakeList) {
                grid[part[0]][part[1]] = 'o';
            }

        }
        else {
            // first run
            for (int r = 0; r < rows; r++) {
                for (int c = 0; c < cols; c++) {
                    if (grid[r][c] == 'o') {
                        snakeList.add(new int[]{r, c});
                    }
                }
            }
        }

        // directions
        String[] dirs = {"up", "down", "left", "right"};
        int[][] moves = {
                        {-1, 0},
                        {1, 0},
                        {0, -1},
                        {0, 1}
                                };

        String direction = args[0];
        int steps = Integer.parseInt(args[1]);

        for (int step = 0; step < steps; step++) {
            int[] head = ((LinkedList<int[]>) snakeList).getLast();
            int newRow = head[0];
            int newCol = head[1];

            for (int i = 0; i < dirs.length; i++) {
                if (direction.equals(dirs[i])) {
                    newRow += moves[i][0];
                    newCol += moves[i][1];
                }
            }

            // Invalid move
            if (snakeList.size() > 1) {
                int[] neck = snakeList.get(snakeList.size() - 2);
                if (newRow == neck[0] && newCol == neck[1]) {
                    System.out.println("Reverse movement is Invalid.");
                    break;
                }
            }

            // Hit the wall (Error Solution)
            if (newRow < 0 || newRow >= rows || newCol < 0 || newCol >= cols) {
                System.out.println("Hit the wall!");
                break;
            }

            // move
            snakeList.add(new int[]{newRow, newCol});
            grid[newRow][newCol] = 'o';

            int[] tail = ((LinkedList<int[]>) snakeList).removeFirst();
            grid[tail[0]][tail[1]] = '-';
        }

        // print
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                System.out.print(grid[r][c] + " ");
            }
            System.out.println();
        }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {
            for (int r = 0; r < rows; r++) {
                for (int c = 0; c < cols; c++) {
                    bw.write(grid[r][c]);
                    if (c < cols - 1) bw.write(" ");
                }
                bw.newLine();
            }
        }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(statePath))) {
            for (int[] part : snakeList) {
                bw.write(part[0] + "," + part[1]);
                bw.newLine();
            }
        }
    }
}
