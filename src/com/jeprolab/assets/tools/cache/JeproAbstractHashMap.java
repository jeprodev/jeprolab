package com.jeprolab.assets.tools.cache;

import com.jeprolab.JeproLab;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.*;

/**
 *
 * Created by jeprodev on 17/01/2013.
 */
public class JeproAbstractHashMap<K, V> extends AbstractMap<K, V> implements JeproIterableMap<K, V> {
    protected static final String NO_NEXT_ENTRY = JeproLab.getBundle().getString("JEPROLAB_LABEL");
    protected static final String NO_PREVIOUS_ENTRY  = JeproLab.getBundle().getString("JEPROLAB_LABEL");
    protected static final String INVALID_REMOVE  = JeproLab.getBundle().getString("JEPROLAB_LABEL");
    protected static final String GET_INVALID_KEY  = JeproLab.getBundle().getString("JEPROLAB_LABEL");
    protected static final String GET_INVALID_VALUE  = JeproLab.getBundle().getString("JEPROLAB_LABEL");
    protected static final String SET_INVALID_VALUES = JeproLab.getBundle().getString("JEPROLAB_LABEL");

    /**
     * The default capacity to use.
     */
    protected static final int DEFAULT_CAPACITY = 16;
    protected static final int DEFAULT_THRESHOLD = 12;
    protected static final float DEFAULT_LOAD_FACTOR =  0.75f;
    protected static final int MAXIMUM_CAPACITY = 1 << 30;

    protected static final Object NULL = new Object();

    /**
     * Load factor, normally 0.75;
     */
    transient float loadFactor;
    transient int entrySize;
    transient JeproHashEntry<K, V>[]entryData;
    transient int threshold;
    transient int modCount;
    transient JeproEntrySet<K,V> entrySet;
    transient JeproKeySet<K> keySet;
    transient JeproValues<V> values;

    protected JeproAbstractHashMap(){
        super();
    }

    protected JeproAbstractHashMap(final int initialCapacity, final float loadFactor, final int threshold){
        super();
        this.loadFactor = loadFactor;
        this.entryData = new JeproHashEntry[initialCapacity];
        this.threshold = threshold;
        init();
    }

    protected JeproAbstractHashMap(final int initialCapacity){
        this(initialCapacity, DEFAULT_LOAD_FACTOR);
    }

    protected JeproAbstractHashMap(int initialCapacity, final  float loadFactor){
        super();
        if(initialCapacity < 0){
            throw new IllegalArgumentException(JeproLab.getBundle().getString("JEPROLAB_LABEL"));
        }

        if(loadFactor <= 0.0f || Float.isNaN(loadFactor)){
            throw new IllegalArgumentException(JeproLab.getBundle().getString("JEPROLAB_LABEL"));
        }
        this.loadFactor = loadFactor;
        initialCapacity = computeNewInitalCapacity(initialCapacity, loadFactor);
        this.threshold = computeThreshold(initialCapacity, loadFactor);
        this.entryData = new JeproHashEntry[initialCapacity];
        init();
    }

    protected JeproAbstractHashMap(final Map<? extends K, ? extends V> map){
        this(Math.max(2 * map.size(), DEFAULT_CAPACITY), DEFAULT_LOAD_FACTOR);
        putAll(map);
    }

    protected void init(){}

    public V get(Object key){
        key = convertKey(key);
        final int hashCode = hash(key);
        JeproHashEntry<K, V> entry = entryData[hashIndex(hashCode, entryData.length)];
        while(entry != null){
            if(entry.hashCode == hashCode && isEqualKey(key, entry.key)){
                return entry.getValue();
            }
            return entry.next;
        }
        return null;
    }

    public int size(){
        return entrySize;
    }

    public boolean isEmpty(){
        return entrySize == 0;
    }

    public boolean containsKey(Object key){
        key = convertKey(key);
        final int hashCode = hash(key);
        JeproHashEntry<K, V> entry = entryData[hashIndex(hashCode, entryData.length)];
        while(entry != null){
            if(entry.hashCode == hashCode && isEqualKey(key, entry.key)){
                return true;
            }
            entry = entry.next;
        }
        return false;
    }

    public boolean containsValue(final Object value){
        if(value == null){
            for(final JeproHashEntry<K, V> element :  entryData){
                JeproHashEntry<K, V> entry = element;
                while (entry != null){
                    if(entry.getValue() == null){
                        return true;
                    }
                    entry = entry.next;
                }
            }
        }else{
            for(final JeproHashEntry<K, V> element : entryData){
                JeproHashEntry<K, V> entry = element;
                while (entry != null){
                    if(isEqualValue(value, entry.getValue())){
                        return true;
                    }
                    entry = entry.next;
                }
            }
        }
        return false;
    }

    public V put(final K key, final V value){
        final Object convertedKey = convertKey(key);
        final int hashCode = hash(convertedKey);
        final int index = hashIndex(hashCode, entryData.length);
        JeproHashEntry<K, V> entry = entryData[index];
        while(entry != null){
            if(entry.hashCode == hashCode && isEqualKey(convertedKey, entry.key)){
                final V oldValue = entry.getValue();
                updateEntry(entry, value);
                return oldValue;
            }
            entry = entry.next;
        }
        addMapping(index, hashCode, key, value);
        return null;
    }

    @Override
    public void putAll(final Map<? extends K, ? extends V> map){
        putAllEntries(map);
    }

    private void putAllEntries(final Map<? extends K, ? extends V> map){
        final int mapSize = map.size();
        if(!(mapSize == 0)){
            final int newSize = (int)((entrySize + mapSize)/ loadFactor + 1);
            ensureCapacity(computeNewCapacity(newSize));
            for(final Map.Entry<? extends K, ? extends V> entry : map.entrySet()){
                put(entry.getKey(), entry.getValue());
            }
        }
    }

    public V remove(Object key){
        key = convertKey(key);
        final int hashCode = hash(key);
        final int index = hashIndex(hashCode, entryData.length);
        JeproHashEntry<K, V> entry = entryData[index];
        JeproHashEntry<K, V> previous = null;
        while(entry != null){
            if(entry.hashCode == hashCode && isEqualKey(key, entry.key)){
                final V oldValue = entry.getValue();
                removeMapping(entry, index, previous);
                return oldValue;
            }
            previous = entry;
            entry = entry.next;
        }
        return null;
    }

    public void clear(){
        modCount++;
        final JeproHashEntry<K, V>[] data = this.entryData;
        for(int i = data.length - 1; i >= 0; i--){
            data[i] = null;
        }
        entrySize = 0;
    }

    protected Object convertKey(final Object key){
        return key == null ? NULL : key;
    }

    protected int hash(final Object key){
        int h = key.hashCode();
        h += ~(h << 9);
        h ^= h >>> 14;
        h += h << 4;
        h ^= h >>> 10;
        return h;
    }

    protected boolean isEqualKey(final Object keyOne, final Object keyTwo){
        return keyOne == keyTwo ||keyOne.equals(keyTwo);
    }

    protected boolean isEqualValue(final Object valueOne, final Object valueTwo){
        return valueOne == valueTwo ||valueOne.equals(valueTwo);
    }

    protected int hashIndex(final int hashCode, final int dataSize){
        return hashCode & dataSize - 1;
    }

    protected JeproHashEntry<K, V> getEntry(Object key){
        key = convertKey(key);
        final int hashCode = hash(key);
        JeproHashEntry<K, V> entry = entryData[hashIndex(hashCode, entryData.length)];
        while(entry != null){
            if(entry.hashCode == hashCode && isEqualKey(key, entry.key)){
                return entry;
            }
            entry = entry.next;
        }
        return null;
    }

    protected void updateEntry(final JeproHashEntry<K, V> entry, final V newValue){
        entry.setValue(newValue);
    }

    protected void reuseEntry(final JeproHashEntry<K, V> entry, final int hashIndex, final int hashCode, final K key, final V val){
        entry.next = entryData[hashIndex];
        entry.hashCode = hashCode;
        entry.key = key;
        entry.value = val;
    }

    protected void addMapping(final int hashIndex, final int hashCode, final K key, final V value){
        final JeproHashEntry<K, V> entry = createEntry(entryData[hashIndex], hashCode, key, value);
        addEntry(entry, hashIndex);
        entrySize++;
        checkCapacity();
    }

    protected JeproHashEntry<K, V> createEntry(JeproHashEntry<K, V> nextEntry, final int hashCode, final K key, final V val){
        return new JeproHashEntry<>(nextEntry, hashCode, key, val);
    }

    protected void addEntry(final JeproHashEntry<K, V> entry, final int hashIndex){
        entryData[hashIndex] = entry;
    }

    protected void removeMapping(final JeproHashEntry<K, V> entry, final int hashIndex, final JeproHashEntry<K, V> previous){
        modCount++;
        removeEntry(entry, hashIndex, previous);
        entrySize--;
        destroyEntry(entry);
    }

    protected void removeEntry(final JeproHashEntry<K, V> entry, final int hashIndex, final JeproHashEntry<K, V> previous) {
        if (previous == null) {
            entryData[hashIndex] = entry.next;
        }else{
            previous.next = entry.next;
        }
    }

    protected void destroyEntry(final JeproHashEntry<K, V> entry){
        entry.next = null;
        entry.key = null;
        entry.value = null;
    }

    protected K entryKey(final JeproHashEntry<K, V> entry){
        return entry.getKey();
    }

    protected V entryValue(final JeproHashEntry<K, V> entry){
        return entry.getValue();
    }

    protected void checkCapacity(){
        if(entrySize >= threshold){
            final int newCapacity = entryData.length * 2;
            if(newCapacity <= MAXIMUM_CAPACITY){
                ensureCapacity(newCapacity);
            }
        }
    }

    protected void ensureCapacity(final int newCapacity){
        final int oldCapacity = entryData.length;
        if(newCapacity > oldCapacity){
            if(entrySize ==0){
                threshold = computeThreshold(newCapacity, loadFactor);
                entryData = new JeproHashEntry[newCapacity];
            }else{
                final  JeproHashEntry<K, V> oldEntries[] = entryData;
                final  JeproHashEntry<K, V> newEntries[] = new JeproHashEntry[newCapacity];

                modCount++;
                for(int i = oldCapacity - 1; i >= 0; i--){
                    JeproHashEntry<K, V> entry = oldEntries[i];
                    if(entry != null){
                        oldEntries[i] = null;
                        do{
                            final JeproHashEntry<K, V> next = entry.next;
                            final int index = hashIndex(entry.hashCode, newCapacity);

                            entry.next = newEntries[index];
                            newEntries[index] = entry;
                            entry = next;
                        }while(entry !=null);
                    }
                }
                threshold = computeThreshold(newCapacity, loadFactor);
                entryData = newEntries;
            }
        }
    }

    public MapIterator<K, V> mapIterator(){
        if(entrySize == 0){
            return  JeproEmptyMapIterator.<K, V>emptyMapIterator()
        }
        return new JeproHashMapIterator<K, V>(this);
    }

    @Override
    public Set<Entry<K, V>> entrySet(){
        if(entrySet == null){
            entrySet = new JeproEntrySet<>(this);
        }
        return entrySet;
    }

    @Override
    public Set<K> keySet(){
        if(keySet == null){
            keySet = new JeproKeySet<K>(this);
        }
        return keySet;
    }

    protected Iterator<Map.Entry<K, V>> createEntrySetIterator(){
        if(size() == 0){
            return JeproEmptyIterator.<Map.Entry<K, V>>emptyIterator();
        }
        return new JeproSetIterator<K, V>(this);
    }

    public boolean equals(final Object obj){
        if(obj == this){
            return true;
        }

        if(!(obj instanceof Map)){
            return false;
        }
        final Map<?, ?> map = (Map<?, ?>) obj;
        if(map.size() != size()){
            return false;
        }

        final JeproMapIterator<?, ?> it = mapIterator();
        try {
            while(it.hasNext()){
                final Object key = it.next();
                final Object value = it.getValue();
                if(value == null){
                    if(map.get(key) != null || !map.containsKey(key)){
                        return false;
                    }
                }else{
                    if(!value.equals(map.get(key))){
                        return false;
                    }
                }
            }
        }catch(final ClassCastException | NullPointerException ignored){
            return false;
        }
        return true;
    }

    public int hashCode(){
        int total = 0;
        final Iterator<Map.Entry<K, V>> it = createEnttrySetIterator();
        while(it.hasNext()){
            total += it.next().hashCode();
        }
        return total;
    }

    public  String toString(){
        if(size() == 0){
            return "{}";
        }
        final StringBuilder buf = new StringBuilder(32 * size());
        buf.append("{");
        final MapIterator<K, V> it = mapIterator();
        boolean hasNext = it.hasNext();
        while(hasNext){
            final K key = it.next();
            final V val = it.getValue();
            buf.append(key == this ? "(this Map)" : key).append("=").append(val == this ? "(this Map)" : val);
            hasNext = it.hasNext();
            if(hasNext){ buf.append(", "); }
        }
        buf.append("}");
        return buf.toString();
    }

    protected JeproAbstractHashMap<K, V> clone(){
        try{
            final JeproAbstractHashMap<K, V> cloned = (JeproAbstractHashMap<K, V>) super.clone();
            cloned.entryData = new JeproHashEntry[entryData.length];
            cloned.entrySet = null;
            cloned.keySet = null;
            cloned.modCount = 0;
            cloned.values = null;
            cloned.entrySize = 0;
            cloned.init();
            cloned.putAll(this);
            return cloned;
        }catch (final CloneNotSupportedException ex){
            throw new InternalError();
        }
    }

    protected Iterator<V> createValuesIterator(){
        if(size() == 0){
            return EmptyIterator.<V>emptyIterator();
        }
        return new JeproValuesIterator<V>(this);
    }

    protected Iterator<K> createKeySetIterator(){
        if(size() == 0){
            return EmptyIterator.<K>emptyIterator();
        }
        return new JeproKeySetIterator<K>(this);
    }

    public Collection<V> values(){
        if(values == null){
            values = new JeproValues<V>(this);
        }
        return values;
    }

    protected void doWriteObject(final ObjectOutputStream out) throws IOException{
        out.writeFloat(loadFactor);
        out.writeInt(entryData.length);
        out.writeInt(entrySize);
        for(final MapIterator<K,V> it = mapIterator(); it.hasNext();){
            out.writeObject(it.next());
            out.writeObject(it.getValue());
        }
    }

    /**==== Sub Classes ===**/

    protected static  class JeproEntrySet<K, V> extends AbstractSet<Entry<K, V>> {
        private final JeproAbstractHashMap<K, V> parent;

        protected JeproEntrySet(final JeproAbstractHashMap<K, V> prt) {
            super();
            this.parent = prt;
        }

        @Override
        public int size(){
            return parent.size();
        }

        @Override
        public void clear(){
            parent.clear();
        }

        public boolean contains(final Object entry){
            if(entry instanceof Map.Entry){
                final Map.Entry<?, ?> e = (Map.Entry<?, ?>) entry;
                final Entry<K, V> match = parent.getEntry(e.getKey());
                return match != null && match.equals(e);
            }
            return false;
        }

        public boolean remove(final Object obj){
            if(!(obj instanceof Entry)){
                return false;
            }

            if(!contains(obj)){
                return false;
            }
            final Map.Entry<?, ?> entry = (Map.Entry<?, ?>) obj;
            parent.remove(entry.getKey());
            return true;
        }

        public Iterator<Map.Entry<K, V>> iterator(){
            return parent.createEntrySetIterator();
        }
    }

    protected static class JeproKeySet<K> extends AbstractSet<K>{
        private final JeproAbstractHashMap<K, ?> parent;

        protected JeproKeySet(final JeproAbstractHashMap<K, ?> prt){
            super();
            this.parent = prt;
        }

        public int size(){
            return parent.size();
        }

        public void clear(){
            parent.clear();
        }

        public boolean contains(final Object key){
            return parent.containsKey(key);
        }

        public boolean remove(final Object key){
            final boolean result = parent.containsKey(key);
            parent.remove(key);
            return result;
        }

        public Iterator<K> iterator(){
            return parent.createKeySetIterator();
        }
    }

    protected static class JeproHashMapIterator<K, V> extends JeproHashIterator<K, V> implements MapIterator<K, V>{
        protected JeproHashMapIterator(final JeproAbstractHashMap<K, V> prt){
            super(prt);
        }

        public K next(){
            return super.nextEntry().getKey();
        }

        public K getKey(){
            final JeproHashEntry<K, V> current = currentEnntry();
            if(curret == null){
                throw new IllegalStateException(JeproAbstractHashMap.GET_INVALID_KEY)
            }
            return current.getKey()
        }

        public V getValue(){
            final JeproHashEntry<K, V> current = currentEntry();
            if(current == null){
                throw new IllegalStateException(JeproAbstractHashMap.GET_INVALID_VALUE);
            }
            return current.getValue();
        }

        public V setValue(final V val){
            final JeproHashEntry<K, V> current = currentEntry();
            if(current == null){
                throw new IllegalStateException(JeproAbstractHashMap.SET_INVALID_VALUES);
            }
            return current.setValue(val);
        }
    }


    protected static class JeproEntrySetIterator<K, V> extends JeproHashIterator<K, V> implements Iterator<Map.Entry<K, V>>{
        protected JeproEntrySetIterator(final JeproAbstractHashMap<K, V> prt){
            super(prt);
        }

        public Map.Entry<K, V> next(){
            return super.nextEntry();
        }
    }

    protected static abstract class JeproHashIterator<K, V>{
        private final JeproAbstractHashMap<K, V> parent;

        private int hashIndex;

        private  JeproHashEntry<K, V> last;

        private JeproHashEntry<K, V> next;

        private int expectedModCount;

        protected JeproHashIterator(final JeproAbstractHashMap<K,V> prt){
            super();
            this.parent = prt;
            final JeproHashEntry<K, V>[] data = parent.entryData;
            int i = data.length;
            JeproHashEntry<K, V> next = null;
            while(i > 0 && next == null){
                next = data[i--];
            }
            this.next = next;
            this.hashIndex = i;
        }
    }

    protected static class JeproKeySetIterator<K> extends JeproHashIterator<K, Object> implements Iterator<K> {
        protected JeproKeySetIterator(final JeproAbstractHashMap<K, ?> prt){
            super((JeproAbstractHashMap<K, Object>) prt);
        }

        public K next(){
            return super.nextEntry().getKey();
        }
    }

    protected static class JeproHashEntry<K, V> implements Map.Entry<K, V>, JeproKeyValue<K, V>{
        protected JeproHashEntry<K, V> next;

        protected int hashCode;

        protected Object key;

        protected Object value;

        protected JeproHashEntry(final JeproHashEntry<K, V> nextEntry, int entryHashCode, final Object entryKey, final V entryValue){
            super();
            this.next = nextEntry;
            this.hashCode = entryHashCode;
            this.key = entryKey;
            this.value = entryValue;
        }

        public K getKey(){
            if(key == null){
                return null;
            }
            return (K) key;
        }

        public V getValue(){
            return (V)value;
        }

        public V setValue(final V val){
            final Object old = this.value;
            this.value = val;
            return (V) old;
        }

        public boolean equals(final Object obj){
            if(obj == this){
                return true;
            }

            if(!(obj instanceof Map.Entry)){
                return false;
            }
            final Map.Entry<?, ?> other = (Map.Entry<?, ?>) obj;
            return (getKey() == null ? other.getKey() == null : getKey().equals(other.getKey())) && (getValue() == null ? other.getValue() == null : getValue().equals(other.getValue()));
        }

        public int hashCode(){
            return (getKey() == null ? 0 : getKey().hashCode())^(getValue() == null ? 0 : getValue().hashCode());
        }

        public String toString(){
            return new StringBuilder().append(getKey()).append("=").append(getValue()).toString();
        }
    }

    protected static class JeproValues<V> extends AbstractCollection<V>{
        private final JeproAbstractHashMap<?, V> parent;

        protected JeproValues(final JeproAbstractHashMap<?, V> prt){
            super();
            this.parent = prt;
        }

        public int size(){
            return parent.size();
        }

        public boolean contains(final Object val){
            return parent.containsValue(val);
        }

        public Iterator<V> iterator(){
            return parent.createValuesIterator();
        }
    }

    protected static class JeproValuesIterator<V> extends JeproHashIterator<Object, V> implements Iterator<V> {
        protected JeproValuesIterator(final JeproAbstractHashMap<?, V> prt){
            super((JeproAbstractHashMap<Object, V>) prt);
        }
        public V next(){
            return super.nextEntry().getValue();
        }
    }

    protected static abstract class JeproHashIterator<K, V>{
        private final JeproAbstractHashMap<K, V> parent;

        private  int hashIndex;

        private JeproHashEntry<K, V> last;
        private JeproHashEntry<K, V> next;

        private int expectedModCount;

        protected JeproHashIterator(final JeproAbstractHashMap<K, V> prt){
            super();
            this.parent = prt;
            final JeproHashEntry<K, V>[] data = parent.entryData;
            int i = data.length;
            JeproHashEntry<K, V> nextEntry = null;
            while(nextEntry == null && i > 0){
                nextEntry = data[i--];
            }
            this.next = nextEntry;
            this.hashIndex = 1;
            this.expectedModCount = parent.modCount;
        }

        public boolean hasNext(){
            return next != null;
        }

        protected JeproHashEntry<K, V> nextEntry(){
            if(parent.modCount != expectedModCount){
                throw new ConcurrentModificationException();
            }

            final JeproHashEntry<K, V> newCurrent = next;
            if(newCurrent == null){
                throw new NoSuchElementException(JeproAbstractHashMap.NO_NEXT_ENTRY);
            }

            final JeproHashEntry<K, V>[] data = parent.entryData;
            int i = hashIndex;
            JeproHashEntry<K, V> n = newCurrent.next;
            while(n == null && i > 0){
                n = data[--i];
            }
            next = n;
            hashIndex = i;
            last = newCurrent;
            return newCurrent;
        }

        protected JeproHashEntry<K, V> currentEntry(){
            return last;
        }

        public void remove(){
            if(last == null){
                throw new IllegalStateException(JeproAbstractHashMap.INVALID_REMOVE);
            }

            if(parent.modCount != expectedModCount){
                throw new ConcurrentModificationException();
            }

            parent.remove(last.getKey());
            last = null;
            expectedModCount = parent.modCount;
        }

        public String toString(){
            String iterator = "Iterator[";
            if(last != null){
                iterator += last.getKey() + "=" + last.getValue();
            }
            return iterator + "]";
        }
    }
}