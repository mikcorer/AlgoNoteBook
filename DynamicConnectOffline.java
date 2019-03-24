import java.io.*;
import java.util.*;

/** https://codeforces.com/gym/100551/problem/A */

public class DynamicConnectOffline {

    public static void main(String[] args) throws Exception {
        new DynamicConnectOffline().go();
    }

    PrintWriter out;
    Reader in;

    DynamicConnectOffline() throws IOException {

        try {
            in = new Reader("connect.in");
            out = new PrintWriter( new BufferedWriter(new FileWriter("connect.out")) );
        }
        catch (Exception e) {
            in = new Reader();
            out = new PrintWriter( new BufferedWriter(new OutputStreamWriter(System.out)) );
        }
    }

    void go() throws Exception {

        //int t = in.nextInt();
        int t = 1;
        while (t > 0) {
            solve();
            t--;
        }

        out.flush();
        out.close();
    }


    int inf = 2000000000;
    int mod = 1000000007;
    double eps = 0.000000001;

    void solve() throws IOException {
        int n = in.nextInt();
        int q = in.nextInt();

        if (q == 0)
            return;

        TreeSet<Edge> edges = new TreeSet<>();
        SegmentTree t = new SegmentTree(q, n);
        ArrayList<Integer> quests = new ArrayList<>();
        for (int i = 0; i < q; i++) {
            String query = in.next();
            if (query.equals("+") || query.equals("-")) {
                int v = in.nextInt() - 1;
                int u = in.nextInt() - 1;

                if (v > u) {
                    v ^= u;
                    u ^= v;
                    v ^= u;
                }

                if (query.equals("+")) {
                    edges.add(new Edge(v, u, i));
                } else {
                    Edge e = edges.ceiling(new Edge(v, u, 0));
                    edges.remove(e);
                    t.addEdge(e.i, i - 1, new Pair(v, u));
                }

            } else {
                quests.add(i);
            }
        }

        for (Edge e : edges)
            t.addEdge(e.i, q - 1, new Pair(e.v, e.u));

        int[] ans = new int[q];
        t.go(1, 0, q - 1, ans);
        for (int i : quests)
            out.println(ans[i]);
    }

    class DSU {

        int[] set;
        int[] rank;
        int k;
        LinkedList<Pair> hist = new LinkedList();

        DSU(int n) {
            this.k = n;
            set = new int[n];
            rank = new int[n];
            Arrays.fill(rank, 1);
            for (int i = 0; i < n; i++)
                set[i] = i;
        }

        int get(int v) {
            if (set[v] == v)
                return v;
            return get(set[v]);
        }

        void union(int a, int b) {
            a = get(a);
            b = get(b);
            if (a == b)
                return;

            if (rank[a] < rank[b]) {
                a ^= b;
                b ^= a;
                a ^= b;
            }

            hist.push(new Pair(a, b));
            hist.push(new Pair(set[b], rank[a]));
            set[b] = a;
            rank[a] += rank[b];
            k--;
        }

        void rollback() {
            Pair info = hist.pop();
            Pair it = hist.pop();
            set[it.b] = info.a;
            rank[it.a] = info.b;
            k++;
        }

    }

    class SegmentTree {

        private ArrayList<Pair>[] t;
        int n;
        DSU dsu;

        SegmentTree(int n, int k) {
            n = Math.max(n, 1);
            this.n = n;
            t = new ArrayList[n * 4];
            dsu = new DSU(k);
        }

        void addEdge(int v, int tl, int tr, int l, int r, Pair e) {
            if (l > r)
                return;

            if (tl == l && tr == r) {
                if (t[v] == null)
                    t[v] = new ArrayList<>();
                t[v].add(e);
                return;
            }

            int tm = (tl + tr) / 2;
            addEdge(v * 2, tl, tm, l, Math.min(tm, r), e);
            addEdge(v * 2 + 1, tm + 1, tr, Math.max(tm + 1, l), r, e);
        }

        void addEdge(int l, int r, Pair e) {
            addEdge(1, 0, n - 1, l, r, e);
        }

        void go(int v, int tl, int tr, int[] ans) {
            int size = dsu.hist.size();

            if (t[v] != null) {
                for (Pair p : t[v]) {
                    dsu.union(p.a, p.b);
                }
            }

            if (tl == tr) {
                ans[tl] = dsu.k;
                return;
            }

            int tm = (tl + tr) / 2;
            go(v * 2, tl, tm, ans);
            go(v * 2 + 1, tm + 1, tr, ans);

            while (dsu.hist.size() > size)
                dsu.rollback();
        }

    }

    class Edge implements Comparable<Edge> {

        int v;
        int u;
        int i;

        Edge(int v, int u, int i) {
            this.v = v;
            this.u = u;
            this.i = i;
        }

        public int compareTo(Edge e) {
            if (v != e.v)
                return Integer.compare(v, e.v);
            else
                return Integer.compare(u, e.u);
        }
    }

    class Pair implements Comparable<Pair> {

        int a;
        int b;

        Pair(int a, int b) {

            this.a = a;
            this.b = b;
        }

        public int compareTo(Pair p) {
            if (a != p.a)
                return Integer.compare(a, p.a);
            else
                return Integer.compare(b, p.b);
        }
    }

    class Item {

        int a;
        int b;
        int c;

        Item(int a, int b, int c) {
            this.a = a;
            this.b = b;
            this.c = c;
        }

    }


    class Reader {

        BufferedReader  br;
        StringTokenizer tok;

        Reader(String file) throws IOException {
            br = new BufferedReader( new FileReader(file) );
        }

        Reader() throws IOException {
            br = new BufferedReader( new InputStreamReader(System.in) );
        }

        String next() throws IOException {

            while (tok == null || !tok.hasMoreElements())
                tok = new StringTokenizer(br.readLine());
            return tok.nextToken();
        }

        int nextInt() throws NumberFormatException, IOException {
            return Integer.valueOf(next());
        }

        long nextLong() throws NumberFormatException, IOException {
            return Long.valueOf(next());
        }

        double nextDouble() throws NumberFormatException, IOException {
            return Double.valueOf(next());
        }


        String nextLine() throws IOException {
            return br.readLine();
        }

    }

    static class InputReader
    {
        final private int BUFFER_SIZE = 1 << 16;
        private DataInputStream din;
        private byte[] buffer;
        private int bufferPointer, bytesRead;

        public InputReader()
        {
            din = new DataInputStream(System.in);
            buffer = new byte[BUFFER_SIZE];
            bufferPointer = bytesRead = 0;
        }

        public InputReader(String file_name) throws IOException
        {
            din = new DataInputStream(new FileInputStream(file_name));
            buffer = new byte[BUFFER_SIZE];
            bufferPointer = bytesRead = 0;
        }

        public String readLine() throws IOException
        {
            byte[] buf = new byte[64]; // line length
            int cnt = 0, c;
            while ((c = read()) != -1)
            {
                if (c == '\n')
                    break;
                buf[cnt++] = (byte) c;
            }
            return new String(buf, 0, cnt);
        }

        public int nextInt() throws IOException
        {
            int ret = 0;
            byte c = read();
            while (c <= ' ')
                c = read();
            boolean neg = (c == '-');
            if (neg)
                c = read();
            do
            {
                ret = ret * 10 + c - '0';
            }  while ((c = read()) >= '0' && c <= '9');

            if (neg)
                return -ret;
            return ret;
        }

        public long nextLong() throws IOException
        {
            long ret = 0;
            byte c = read();
            while (c <= ' ')
                c = read();
            boolean neg = (c == '-');
            if (neg)
                c = read();
            do {
                ret = ret * 10 + c - '0';
            }
            while ((c = read()) >= '0' && c <= '9');
            if (neg)
                return -ret;
            return ret;
        }

        public double nextDouble() throws IOException
        {
            double ret = 0, div = 1;
            byte c = read();
            while (c <= ' ')
                c = read();
            boolean neg = (c == '-');
            if (neg)
                c = read();

            do {
                ret = ret * 10 + c - '0';
            }
            while ((c = read()) >= '0' && c <= '9');

            if (c == '.')
            {
                while ((c = read()) >= '0' && c <= '9')
                {
                    ret += (c - '0') / (div *= 10);
                }
            }

            if (neg)
                return -ret;
            return ret;
        }

        private void fillBuffer() throws IOException
        {
            bytesRead = din.read(buffer, bufferPointer = 0, BUFFER_SIZE);
            if (bytesRead == -1)
                buffer[0] = -1;
        }

        private byte read() throws IOException
        {
            if (bufferPointer == bytesRead)
                fillBuffer();
            return buffer[bufferPointer++];
        }

        public void close() throws IOException
        {
            if (din == null)
                return;
            din.close();
        }
    }

}