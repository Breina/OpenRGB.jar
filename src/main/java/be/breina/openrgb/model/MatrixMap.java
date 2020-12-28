package be.breina.openrgb.model;

import static lombok.AccessLevel.PRIVATE;

import java.io.IOException;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import be.breina.openrgb.util.LittleEndianInputStream;

@ToString
@NoArgsConstructor(access = PRIVATE)
@Getter
public class MatrixMap {

    private int height;
    private int width;
    private int[][] matrix;

    MatrixMap(LittleEndianInputStream input) throws IOException {
        height = input.readInt();
        width = input.readInt();

        matrix = new int[height][width];

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                matrix[i][j] = input.readInt();
            }
        }
    }
}
