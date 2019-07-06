import java.util.Arrays;

public class DSU {
    int[] set;
    int[] rank;

    DSU(int n) {
        set = new int[n];
        rank = new int[n];
        Arrays.fill(rank, 1);
        for (int i = 0; i < n; i++)
            set[i] = i;
    }

    int get(int x) {
        if (x == set[x]) return x;
        return set[x] = get(set[x]);
    }

    void union(int a, int b) {
        a = get(a);
        b = get(b);
        if (a == b) return;

        if (rank[a] < rank[b]) {
            a ^= b; b ^= a; a ^= b;
        }
        set[b] = a;
        rank[a] += rank[b];
    }

    boolean same(int a, int b) {
        return get(a) == get(b);
    }

}
