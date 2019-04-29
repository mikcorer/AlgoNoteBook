public class SparseTableRmq {
    private int[] log;
    private int[][] st;

    SparseTableRmq(int[] a) {
        int n = a.length;
        int K = 20;
        log = new int[n + 1];
        st = new int[n][K];
        log[1] = 0;

        for (int i = 2; i <= n; i++)
            log[i] = log[i/2] + 1;

        for (int i = 0; i < n; i++) {
            st[i][0] = a[i];
        }

        for (int j = 1; j <= K; j++)
            for (int i = 0; i + (1 << j) <= n; i++) {
                st[i][j] = Math.max(st[i][j - 1], st[i + (1 << (j - 1))][j - 1]);
            }
    }

    int max(int l, int r) {
        int j = log[r - l + 1];
        return Math.max(st[l][j], st[r - (1 << j) + 1][j]);
    }
}