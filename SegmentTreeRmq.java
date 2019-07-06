public class SegmentTreeRmq {
    private int[] t;
    private int n;
    final int INF = Integer.MAX_VALUE;

    SegmentTreeRmq(int n) {
        this.n = n;
        t = new int[4 * n];
    }

    private void update(int v, int tl, int tr, int pos, int val) {
        if (tl == tr) {
            t[v] = val;
            return;
        }

        int tm = (tl + tr) / 2;
        if (pos <= tm)
            update(v * 2, tl, tm, pos, val);
        else
            update(v * 2 + 1, tm + 1, tr, pos, val);

        t[v] = Math.min(t[v * 2], t[v * 2 + 1]);
    }

    private int get(int v, int tl, int tr, int l, int r) {
        if (l > r) return INF;
        if (tl == l && tr == r)
            return t[v];
        int tm = (tl + tr) / 2;
        return Math.min(
            get(v * 2, tl, tm, l, Math.min(r, tm)),
            get(v * 2 + 1, tm + 1, tr, Math.max(l, tm + 1), r)
        );
    }

    void update(int pos, int val) {
        update(1, 0, n - 1, pos, val);
    }

    int get(int l, int r) {
        return get(1, 0, n - 1, l, r);
    }
}
