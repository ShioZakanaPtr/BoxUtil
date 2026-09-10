package org.boxutil.test;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import org.boxutil.util.container.ContainerHasher;
import org.boxutil.util.container.Obj2ObjRHMap;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.results.format.ResultFormatType;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@Warmup(iterations = 2, time = 3, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 2, time = 3, timeUnit = TimeUnit.SECONDS)
@Fork(value = 2, jvmArgs = {"-Xms8G", "-Xmx8G", "-XX:+UseG1GC"})
@Threads(1)
@State(Scope.Benchmark)
public class MapBenchmark {
    private static final int MAP_INIT_SIZE = 1024;
    private static final float MAP_INIT_LOAD_FACTOR = 0.75f;

    private static final int COUNT = 10_000_000;

    private final static Trash[] KEY_SMALL = new Trash[10_000_000];
    private final static Trash[] FULL_HIT_SMALL = new Trash[10_000_000];
    private final static Trash[] HALF_HIT_SMALL = new Trash[10_000_000];
    private final static Trash[] FEW_HIT_SMALL = new Trash[10_000_000];

    private final static Trash[] KEY_BIG = new Trash[10_000_000];
    private final static Trash[] FULL_HIT_BIG = new Trash[10_000_000];
    private final static Trash[] HALF_HIT_BIG = new Trash[10_000_000];
    private final static Trash[] FEW_HIT_BIG = new Trash[10_000_000];

    public MapBenchmark() {}

    {
        final ThreadLocalRandom rnd = ThreadLocalRandom.current();
        for (int i = 0; i < COUNT; i++) {
            KEY_SMALL[i] = new SmallTrash().put(rnd.nextLong(COUNT));
            KEY_BIG[i] = new BigTrash().put(rnd.nextLong(COUNT));
        }

        for (int i = 0; i < COUNT; i++) {
            FULL_HIT_SMALL[i] = KEY_SMALL[rnd.nextInt(COUNT)];
            HALF_HIT_SMALL[i] = rnd.nextBoolean()
                    ? KEY_SMALL[rnd.nextInt(COUNT)]
                    : new SmallTrash().put(rnd.nextLong(COUNT) + COUNT);
            FEW_HIT_SMALL[i] = rnd.nextInt(10) == 0
                    ? KEY_SMALL[rnd.nextInt(COUNT)]
                    : new SmallTrash().put(rnd.nextLong(COUNT) + COUNT);

            FULL_HIT_BIG[i] = KEY_BIG[rnd.nextInt(COUNT)];
            HALF_HIT_BIG[i] = rnd.nextBoolean()
                    ? KEY_BIG[rnd.nextInt(COUNT)]
                    : new BigTrash().put(rnd.nextLong(COUNT) + COUNT);
            FEW_HIT_BIG[i] = rnd.nextInt(10) == 0
                    ? KEY_BIG[rnd.nextInt(COUNT)]
                    : new BigTrash().put(rnd.nextLong(COUNT) + COUNT);
        }
    }

    public interface Trash {
        Trash put(long x);
    }

    public static class SmallTrash implements Trash {
        private int h = 0;
        private int l;

        public Trash put(long x) {
            this.l = Long.hashCode(x);
            return this;
        }

        public int hashCode() {
            int result;
            if ((result = this.h) == 0) {
                result = this.h = this.l ^ super.hashCode();
            }
            return result;
        }
    }

    public static class BigTrash implements Trash {
        private int h, i0;
        private long l0, l1, l2, l3, l4, l5, l6;

        public Trash put(long x) {
            this.i0 = Long.hashCode(x);
            this.l0 = x;
            this.l1 = this.l0 << 1;
            this.l2 = this.l1 << 1;
            this.l3 = this.l2 << 1;
            this.l4 = this.l3 << 1;
            this.l5 = this.l4 << 1;
            this.l6 = this.l5 << 1;
            return this;
        }

        public int hashCode() {
            int result;
            if ((result = this.h) == 0) {
                result = this.h = Long.hashCode(this.i0 ^ (this.l0 * this.l1 * this.l2 * this.l3 * this.l4 * this.l5 * this.l6)) ^ super.hashCode();
            }
            return result;
        }
    }

    @State(Scope.Thread)
    public static class JDK_HashMap_Put {
        Map<Trash, Integer> map;

        public JDK_HashMap_Put() {}

        @Setup(Level.Invocation)
        public void setup() {
            this.map = new HashMap<>(MAP_INIT_SIZE, MAP_INIT_LOAD_FACTOR);
        }
    }

    @Benchmark
    public void bm_JDK_HashMap_PutSmall(JDK_HashMap_Put state, Blackhole bh) {
        for (int i = 0; i < COUNT; i++) {
            bh.consume(state.map.put(KEY_SMALL[i], i));
        }
        bh.consume(state.map.size());
    }

    @Benchmark
    public void bm_JDK_HashMap_PutBig(JDK_HashMap_Put state, Blackhole bh) {
        final var map = state.map;
        for (int i = 0; i < COUNT; i++) {
            bh.consume(map.put(KEY_BIG[i], i));
        }
        bh.consume(map.size());
    }

    @State(Scope.Thread)
    public static class JDK_HashMap_GetSmall {
        Map<Trash, Integer> map;

        public JDK_HashMap_GetSmall() {}

        @Setup(Level.Trial)
        public void setup() {
            this.map = new HashMap<>(MAP_INIT_SIZE, MAP_INIT_LOAD_FACTOR);
            for (int i = 0; i < COUNT; i++) {
                this.map.put(KEY_SMALL[i], i);
            }
        }
    }

    @State(Scope.Thread)
    public static class JDK_HashMap_GetBig {
        Map<Trash, Integer> map;

        public JDK_HashMap_GetBig() {}

        @Setup(Level.Trial)
        public void setup() {
            this.map = new HashMap<>(MAP_INIT_SIZE, MAP_INIT_LOAD_FACTOR);
            for (int i = 0; i < COUNT; i++) {
                this.map.put(KEY_BIG[i], i);
            }
        }
    }

    @Benchmark
    public void bm_JDK_HashMap_GetSmallFull(JDK_HashMap_GetSmall state, Blackhole bh) {
        final var map = state.map;
        for (int i = 0; i < COUNT; i++) {
            bh.consume(map.get(FULL_HIT_SMALL[i]));
        }
    }

    @Benchmark
    public void bm_JDK_HashMap_GetBigFull(JDK_HashMap_GetBig state, Blackhole bh) {
        final var map = state.map;
        for (int i = 0; i < COUNT; i++) {
            bh.consume(map.get(FULL_HIT_BIG[i]));
        }
    }

    @Benchmark
    public void bm_JDK_HashMap_GetSmallHalf(JDK_HashMap_GetSmall state, Blackhole bh) {
        final var map = state.map;
        for (int i = 0; i < COUNT; i++) {
            bh.consume(map.get(HALF_HIT_SMALL[i]));
        }
    }

    @Benchmark
    public void bm_JDK_HashMap_GetBigHalf(JDK_HashMap_GetBig state, Blackhole bh) {
        final var map = state.map;
        for (int i = 0; i < COUNT; i++) {
            bh.consume(map.get(HALF_HIT_BIG[i]));
        }
    }

    @Benchmark
    public void bm_JDK_HashMap_GetSmallFew(JDK_HashMap_GetSmall state, Blackhole bh) {
        final var map = state.map;
        for (int i = 0; i < COUNT; i++) {
            bh.consume(map.get(FEW_HIT_SMALL[i]));
        }
    }

    @Benchmark
    public void bm_JDK_HashMap_GetBigFew(JDK_HashMap_GetBig state, Blackhole bh) {
        final var map = state.map;
        for (int i = 0; i < COUNT; i++) {
            bh.consume(map.get(FEW_HIT_BIG[i]));
        }
    }

    @State(Scope.Thread)
    public static class BoxUtil_Obj2ObjRHMap_Put {
        Map<Trash, Integer> map;

        public BoxUtil_Obj2ObjRHMap_Put() {}

        @Setup(Level.Invocation)
        public void setup() {
            this.map = new Obj2ObjRHMap<>(MAP_INIT_SIZE, MAP_INIT_LOAD_FACTOR, ContainerHasher::hashing_HashMap);
        }
    }

    @Benchmark
    public void bm_BoxUtil_Obj2ObjRHMap_PutSmall(BoxUtil_Obj2ObjRHMap_Put state, Blackhole bh) {
        for (int i = 0; i < COUNT; i++) {
            bh.consume(state.map.put(KEY_SMALL[i], i));
        }
        bh.consume(state.map.size());
    }

    @Benchmark
    public void bm_BoxUtil_Obj2ObjRHMap_PutBig(BoxUtil_Obj2ObjRHMap_Put state, Blackhole bh) {
        final var map = state.map;
        for (int i = 0; i < COUNT; i++) {
            bh.consume(map.put(KEY_BIG[i], i));
        }
        bh.consume(map.size());
    }

    @State(Scope.Thread)
    public static class BoxUtil_Obj2ObjRHMap_GetSmall {
        Map<Trash, Integer> map;

        public BoxUtil_Obj2ObjRHMap_GetSmall() {}

        @Setup(Level.Trial)
        public void setup() {
            this.map = new Obj2ObjRHMap<>(MAP_INIT_SIZE, MAP_INIT_LOAD_FACTOR, ContainerHasher::hashing_HashMap);
            for (int i = 0; i < COUNT; i++) {
                this.map.put(KEY_SMALL[i], i);
            }
        }
    }

    @State(Scope.Thread)
    public static class BoxUtil_Obj2ObjRHMap_GetBig {
        Map<Trash, Integer> map;

        public BoxUtil_Obj2ObjRHMap_GetBig() {}

        @Setup(Level.Trial)
        public void setup() {
            this.map = new Obj2ObjRHMap<>(MAP_INIT_SIZE, MAP_INIT_LOAD_FACTOR, ContainerHasher::hashing_HashMap);
            for (int i = 0; i < COUNT; i++) {
                this.map.put(KEY_BIG[i], i);
            }
        }
    }

    @Benchmark
    public void bm_BoxUtil_Obj2ObjRHMap_GetSmallFull(BoxUtil_Obj2ObjRHMap_GetSmall state, Blackhole bh) {
        final var map = state.map;
        for (int i = 0; i < COUNT; i++) {
            bh.consume(map.get(FULL_HIT_SMALL[i]));
        }
    }

    @Benchmark
    public void bm_BoxUtil_Obj2ObjRHMap_GetBigFull(BoxUtil_Obj2ObjRHMap_GetBig state, Blackhole bh) {
        final var map = state.map;
        for (int i = 0; i < COUNT; i++) {
            bh.consume(map.get(FULL_HIT_BIG[i]));
        }
    }

    @Benchmark
    public void bm_BoxUtil_Obj2ObjRHMap_GetSmallHalf(BoxUtil_Obj2ObjRHMap_GetSmall state, Blackhole bh) {
        final var map = state.map;
        for (int i = 0; i < COUNT; i++) {
            bh.consume(map.get(HALF_HIT_SMALL[i]));
        }
    }

    @Benchmark
    public void bm_BoxUtil_Obj2ObjRHMap_GetBigHalf(BoxUtil_Obj2ObjRHMap_GetBig state, Blackhole bh) {
        final var map = state.map;
        for (int i = 0; i < COUNT; i++) {
            bh.consume(map.get(HALF_HIT_BIG[i]));
        }
    }

    @Benchmark
    public void bm_BoxUtil_Obj2ObjRHMap_GetSmallFew(BoxUtil_Obj2ObjRHMap_GetSmall state, Blackhole bh) {
        final var map = state.map;
        for (int i = 0; i < COUNT; i++) {
            bh.consume(map.get(FEW_HIT_SMALL[i]));
        }
    }

    @Benchmark
    public void bm_BoxUtil_Obj2ObjRHMap_GetBigFew(BoxUtil_Obj2ObjRHMap_GetBig state, Blackhole bh) {
        final var map = state.map;
        for (int i = 0; i < COUNT; i++) {
            bh.consume(map.get(FEW_HIT_BIG[i]));
        }
    }

    @State(Scope.Thread)
    public static class FastUtil_Object2ObjectOpenHashMap_Put {
        Map<Trash, Integer> map;

        public FastUtil_Object2ObjectOpenHashMap_Put() {}

        @Setup(Level.Invocation)
        public void setup() {
            this.map = new Object2ObjectOpenHashMap<>(MAP_INIT_SIZE, MAP_INIT_LOAD_FACTOR);
        }
    }

    @Benchmark
    public void bm_FastUtil_Object2ObjectOpenHashMap_PutSmall(FastUtil_Object2ObjectOpenHashMap_Put state, Blackhole bh) {
        for (int i = 0; i < COUNT; i++) {
            bh.consume(state.map.put(KEY_SMALL[i], i));
        }
        bh.consume(state.map.size());
    }

    @Benchmark
    public void bm_FastUtil_Object2ObjectOpenHashMap_PutBig(FastUtil_Object2ObjectOpenHashMap_Put state, Blackhole bh) {
        final var map = state.map;
        for (int i = 0; i < COUNT; i++) {
            bh.consume(map.put(KEY_BIG[i], i));
        }
        bh.consume(map.size());
    }

    @State(Scope.Thread)
    public static class FastUtil_Object2ObjectOpenHashMap_GetSmall {
        Map<Trash, Integer> map;

        public FastUtil_Object2ObjectOpenHashMap_GetSmall() {}

        @Setup(Level.Trial)
        public void setup() {
            this.map = new Object2ObjectOpenHashMap<>(MAP_INIT_SIZE, MAP_INIT_LOAD_FACTOR);
            for (int i = 0; i < COUNT; i++) {
                this.map.put(KEY_SMALL[i], i);
            }
        }
    }

    @State(Scope.Thread)
    public static class FastUtil_Object2ObjectOpenHashMap_GetBig {
        Map<Trash, Integer> map;

        public FastUtil_Object2ObjectOpenHashMap_GetBig() {}

        @Setup(Level.Trial)
        public void setup() {
            this.map = new Object2ObjectOpenHashMap<>(MAP_INIT_SIZE, MAP_INIT_LOAD_FACTOR);
            for (int i = 0; i < COUNT; i++) {
                this.map.put(KEY_BIG[i], i);
            }
        }
    }

    @Benchmark
    public void bm_FastUtil_Object2ObjectOpenHashMap_GetSmallFull(FastUtil_Object2ObjectOpenHashMap_GetSmall state, Blackhole bh) {
        final var map = state.map;
        for (int i = 0; i < COUNT; i++) {
            bh.consume(map.get(FULL_HIT_SMALL[i]));
        }
    }

    @Benchmark
    public void bm_FastUtil_Object2ObjectOpenHashMap_GetBigFull(FastUtil_Object2ObjectOpenHashMap_GetBig state, Blackhole bh) {
        final var map = state.map;
        for (int i = 0; i < COUNT; i++) {
            bh.consume(map.get(FULL_HIT_BIG[i]));
        }
    }

    @Benchmark
    public void bm_FastUtil_Object2ObjectOpenHashMap_GetSmallHalf(FastUtil_Object2ObjectOpenHashMap_GetSmall state, Blackhole bh) {
        final var map = state.map;
        for (int i = 0; i < COUNT; i++) {
            bh.consume(map.get(HALF_HIT_SMALL[i]));
        }
    }

    @Benchmark
    public void bm_FastUtil_Object2ObjectOpenHashMap_GetBigHalf(FastUtil_Object2ObjectOpenHashMap_GetBig state, Blackhole bh) {
        final var map = state.map;
        for (int i = 0; i < COUNT; i++) {
            bh.consume(map.get(HALF_HIT_BIG[i]));
        }
    }

    @Benchmark
    public void bm_FastUtil_Object2ObjectOpenHashMap_GetSmallFew(FastUtil_Object2ObjectOpenHashMap_GetSmall state, Blackhole bh) {
        final var map = state.map;
        for (int i = 0; i < COUNT; i++) {
            bh.consume(map.get(FEW_HIT_SMALL[i]));
        }
    }

    @Benchmark
    public void bm_FastUtil_Object2ObjectOpenHashMap_GetBigFew(FastUtil_Object2ObjectOpenHashMap_GetBig state, Blackhole bh) {
        final var map = state.map;
        for (int i = 0; i < COUNT; i++) {
            bh.consume(map.get(FEW_HIT_BIG[i]));
        }
    }

    public static void main(String[] args) throws RunnerException {
        Options options = new OptionsBuilder()
                .include(MapBenchmark.class.getSimpleName())
                .result("MapBenchmark.csv")
                .resultFormat(ResultFormatType.CSV)
                .build();
        new Runner(options).run();
    }
}
