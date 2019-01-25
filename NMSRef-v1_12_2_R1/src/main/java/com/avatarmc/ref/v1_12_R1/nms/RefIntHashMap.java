package com.avatarmc.ref.v1_12_R1.nms;

import com.avatarmc.ref.AbstractRefBase;

// @generated
@javax.annotation.Generated("com.avatarmc.ref.maven.ReflectionGenerator")
public class RefIntHashMap extends AbstractRefBase {
    private static final java.lang.invoke.MethodHandles.Lookup lookup = getLookup(net.minecraft.server.v1_12_R1.IntHashMap.class);
    // Type not public: net.minecraft.server.v1_12_R1.IntHashMap.IntHashMapEntry<V>[]
    /**
    * An array of HashEntries representing the heads of hash slot lists
    */
    public static final Accessor<Object, net.minecraft.server.v1_12_R1.IntHashMap> slots = access(lookup, net.minecraft.server.v1_12_R1.IntHashMap.class, "a");
    /**
    * The number of items stored in this map
    */
    public static final Accessor<Integer, net.minecraft.server.v1_12_R1.IntHashMap> count = access(lookup, net.minecraft.server.v1_12_R1.IntHashMap.class, "b");
    /**
    * The grow threshold
    */
    public static final Accessor<Integer, net.minecraft.server.v1_12_R1.IntHashMap> threshold = access(lookup, net.minecraft.server.v1_12_R1.IntHashMap.class, "c");
    /**
    * The scale factor used to determine when to grow the table
    */
    public static final Getter<Float, net.minecraft.server.v1_12_R1.IntHashMap> growFactor = accessFinal(lookup, net.minecraft.server.v1_12_R1.IntHashMap.class, "d");
    private interface Methodg$I$I { int computeHash(int integer); }
    private static final java.lang.invoke.MethodHandle MH_g$I$I = staticMethodAccess(lookup, net.minecraft.server.v1_12_R1.IntHashMap.class, "g", Methodg$I$I.class);
    /**
    * Makes the passed in integer suitable for hashing by a number of shifts
    */
    public static int computeHash(int integer) { try { return (int) MH_g$I$I.invokeExact(integer); } catch(Throwable e) { throw sneakyThrow(e); }}
    private interface Methoda$II$I { int getSlotIndex(int hash, int slotCount); }
    private static final java.lang.invoke.MethodHandle MH_a$II$I = staticMethodAccess(lookup, net.minecraft.server.v1_12_R1.IntHashMap.class, "a", Methoda$II$I.class);
    /**
    * Computes the index of the slot for the hash and slot count passed in.
    */
    public static int getSlotIndex(int hash, int slotCount) { try { return (int) MH_a$II$I.invokeExact(hash, slotCount); } catch(Throwable e) { throw sneakyThrow(e); }}
    /**
    * Returns the object associated to a key
    */
    public static <V> V lookup(net.minecraft.server.v1_12_R1.IntHashMap<V> self, int hashEntry) { return self.get(hashEntry); }
    /**
    * Returns true if this hash table contains the specified item.
    */
    public static boolean containsItem(net.minecraft.server.v1_12_R1.IntHashMap self, int hashEntry) { return self.b(hashEntry); }
    // public static <V> net.minecraft.server.v1_12_R1.IntHashMap.IntHashMapEntry<V> lookupEntry(net.minecraft.server.v1_12_R1.IntHashMap<V> self, int hashEntry);
    /**
    * Adds a key and associated value to this map
    */
    public static <V> void addKey(net.minecraft.server.v1_12_R1.IntHashMap<V> self, int hashEntry, V valueEntry) { self.a(hashEntry, valueEntry); }
    private interface Methodh$I$V { void grow(net.minecraft.server.v1_12_R1.IntHashMap self, int arg0); }
    private static final java.lang.invoke.MethodHandle MH_h$I$V = methodAccess(lookup, net.minecraft.server.v1_12_R1.IntHashMap.class, "h", Methodh$I$V.class);
    /**
    * Increases the number of hash slots
    */
    public static void grow(net.minecraft.server.v1_12_R1.IntHashMap self, int arg0) { try { MH_h$I$V.invokeExact(self, arg0); } catch(Throwable e) { throw sneakyThrow(e); }}
    // Skipping due to private parameter: private void net.minecraft.server.v1_12_R1.IntHashMap.a(net.minecraft.server.v1_12_R1.IntHashMap$IntHashMapEntry[])
    /**
    * Removes the specified object from the map and returns it
    */
    public static <V> V removeObject(net.minecraft.server.v1_12_R1.IntHashMap<V> self, int o) { return self.d(o); }
    // public static net.minecraft.server.v1_12_R1.IntHashMap.IntHashMapEntry<V> removeEntry(net.minecraft.server.v1_12_R1.IntHashMap self, int arg0);
    /**
    * Removes all entries from the map
    */
    public static void clearMap(net.minecraft.server.v1_12_R1.IntHashMap self) { self.c(); }
    private interface Methoda$IILjavaPlangPObjectEI$V<V> { void insert(net.minecraft.server.v1_12_R1.IntHashMap<V> self, int arg0, int arg1, V arg2, int arg3); }
    private static final java.lang.invoke.MethodHandle MH_a$IILjavaPlangPObjectEI$V = methodAccess(lookup, net.minecraft.server.v1_12_R1.IntHashMap.class, "a", Methoda$IILjavaPlangPObjectEI$V.class);
    /**
    * Adds an object to a slot
    */
    public static <V> void insert(net.minecraft.server.v1_12_R1.IntHashMap<V> self, int arg0, int arg1, V arg2, int arg3) { try { MH_a$IILjavaPlangPObjectEI$V.invokeExact(self, arg0, arg1, arg2, arg3); } catch(Throwable e) { throw sneakyThrow(e); }}
}
