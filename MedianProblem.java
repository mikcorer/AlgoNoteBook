import java.io.*;
import java.util.*;

public class MedianProblem {

    public static void main(String[] arg) throws IOException {
        new MedianProblem().solve();
    }

    void solve() throws IOException {

        InputReader in = new InputReader();
        PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(System.out)));

        Multiset set = new Multiset();

        int n = in.nextInt();
        for (int i = 0; i < n; i++) {
            String[] tokens = in.readLine().split(" ");
            int x = Integer.valueOf(tokens[1]);
            long res = Long.MAX_VALUE;
            if (tokens[0].equals("a")) {
                set.add(x);
                //System.err.println(set.median);
                res = set.getMedian();
            }
            else {
                if (set.set.contains(x)) {
                    set.del(x);
                    if (set.size == 0)
                        out.println("Wrong!");
                    else
                        res = set.getMedian();
                }
                else {
                    out.println("Wrong!");
                }
            }

            if (res != Long.MAX_VALUE) {
                if (Math.abs(res) % 2 == 1) {
                    if (res == -1) out.print("-");
                    out.println(res / 2 + ".5");
                }
                else
                    out.println(res/2);
            }
        }
        out.close();
    }

    class Multiset {

        private TreeSet<Integer> set = new TreeSet<>();
        private HashMap<Integer, Integer> map = new HashMap<>();

        int median;
        int counter = 0;
        int size = 0;

        /** Left and right elements count from median */
        int balance = 0;

        void add(int x) {
            size++;

            if (set.contains(x)) {
                map.put(x, map.get(x) + 1);
            } else {
                set.add(x);
                map.put(x, 1);
            }

            if (size == 1) {
                median = x;
                counter = 1;
            } else if (x < median) {
                balance--;
            }
            else {
                balance++;
            }

            rebalance();
        }

        void del(int x) {
            size--;

            if (!set.contains(x))
                return;

            int cnt = map.get(x) - 1;
            map.put(x, cnt);
            if (cnt == 0) {
                set.remove(x);
            }

            if (size == 0)
                return;

            if (x < median) {
                balance++;
            }
            else
            if (x > median) {
                balance--;
            }
            else {
                if (map.get(x) == 0 || counter > map.get(x)) {
                    if (balance > 0) {
                        balance++;
                        next();
                    }
                    else {
                        balance--;
                        prev();
                    }
                }
                else {
                    balance--;
                }
            }
            rebalance();
        }

        private void rebalance() {
            if (balance > 1)
                next();
            else if (balance < 0)
                prev();
        }

        private void next() {
            if (counter < map.get(median)) {
                counter++;
            }
            else {
                median = set.higher(median);
                counter = 1;
            }
            balance -= 2;
        }

        private void prev() {
            if (counter > 1) {
                counter--;
            }
            else {
                median = set.lower(median);
                counter = map.get(median);
            }
            balance += 2;
        }

        long getMedian() {

            System.err.println(balance);

            if (size%2 == 1 || size == 1)
                return median * 2l;

            int next = median;

            if (counter == map.get(median)) {
                next = set.higher(median);
            }

            return (long)next + median;
        }

        int size() {
            return size;
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
