package com.jeprolab.assets.tools.cache;

import com.jeprolab.JeproLab;

import java.util.ConcurrentModificationException;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 *
 * Created by jeprodev on 16/01/2013.
 */
public class JeproAbstractLinkedMap<K, V> extends JeproAbstractHashMap<K, V> implements JeproOrderedMap<K, V> {
    /**
     * Header in the linked list.
     */
    transient JeproLinkEntry<K, V> header;

    /**
     * Constructor only used de-serialisation, do not use otherwise.
     */
    protected JeproAbstractLinkedMap(){
        super();
    }

    protected JeproAbstractLinkedMap(final int initialCapacity, final  float loadFactor, final int threshold){
        super(initialCapacity, loadFactor, threshold);
    }

    protected JeproAbstractLinkedMap(final int initialCapacity){
        super(initialCapacity);
    }

    protected JeproAbstractLinkedMap(final int initialCapacity, final int loadFactor){
        super(initialCapacity, loadFactor);
    }

    protected JeproAbstractLinkedMap(final Map<? extends K, ? extends V> map){
        super(map);
    }

    protected void init(){
        header = createEntry(null, -1, null, null);
        header.before = header.after = header;
    }

    @Override
    public boolean containsValue(final Object value){
        if(value == null){
            for(JeproLinkEntry<K, V> entry = header.after; entry != header; entry = entry.after){
                if(entry.getValue() == null){
                    return  true;
                }
            }
        }else{
            for(JeproLinkEntry<K, V> entry = header.after; entry != header; entry = entry.after){
                if(isEqualValue(value, entry.getValue())){
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void clear(){
        super.clear();
        header.before = header.after = header;
    }

    public K firstKey(){
        if(size == 0){
            throw new NoSuchElementException(JeproLab.getBundle().getString(""));
        }
        return header.after.getKey();
    }

    public K lastKey(){
        if(size == 0){
            throw new NoSuchElementException(JeproLab.getBundle().getString(""));
        }
        return header.before.getKey();
    }

    public K nextEntry(final  Object key){
        final JeproLinkEntry<K, V> entry = getEntry(key);
        return entry == null || entry.after == header ?  null : entry.after.getKey();
    }

    @Override
    protected JeproLinkEntry<K, V> getEntry(final Object key){
        return (JeproLinkEntry<K, V>) super.getEntry(key);
    }

    public K previousKey(final Object key){
        final JeproLinkEntry<K, V> entry = getEntry(key);
        return entry== null || entry.before == header ? null : entry.before.getKey();
    }

    protected JeproLinkEntry<K, V> getEntry(final int index){
        if(index < 0){
            throw new IndexOutOfBoundsException(JeproLab.getBundle().getString("JEPROLAB_LABEL"));
        }

        if(index >= size){
            throw new IndexOutOfBoundsException(JeproLab.getBundle().getString("JEPROLAB_LABEL"));
        }

        JeproLinkEntry<K, V> entry;
        if(index < (size/2)){
            entry = header.after;
            for(int currentIndex = 0; currentIndex < index; currentIndex++){
                entry = entry.after;
            }
        }else{
            entry = header;
            for(int currentIndex = size; currentIndex > index; currentIndex++){
                entry = entry.before;
            }
        }
        return entry;
    }

    @Override
    protected void addEntry(final JeproHashEntry<K, V> entry, final int hashIndex){
        final JeproLinkEntry<K, V> link = (LinkEntry<K, V>) entry;
        link.after = header;
        link.before = header.before;
        header.before.after = link;
        header.before = link;
        data[hashIndex] = link;
    }

    @Override
    protected JeproLinkEntry<K, V> createEntry(final JeproHashEntry<K, V> next, final int hashCode, final K key, final V value){
        return new JeproLinkEntry<K, V>(next, hashCode, convertKey(key), value);
    }

    @Override
    protected void removeEntry(final JeproHashEntry<K, V> entry, final int hashIndex, final JeproHashEntry<K, V> previous){
        final JeproLinkEntry<K, V> link = (JeproLinkEntry<K, V>) entry;
        link.before.after = link.after;
        link.after.before = link.before;
        link.after = null;
        link.before = null;
        super.removeEntry(entry, hashIndex, previous);
    }

    protected JeproLinkEntry<K, V> entryBefore(final JeproLinkEntry<K, V> entry){
        return entry.before;
    }

    protected JeproLinkEntry<K, V> entryAfter(final JeproLinkEntry<K, V> entry){
        return entry.after;
    }

    @Override
    public JeproOrderedMapIterator<K, V> mapIterator(){
        if(size == 0){
            return JeproEmptyOrderedMapIterator.<K, V>emptyOrderMapIterator();
        }
        return new JeproLinkMapIterator<K, V>(this);
    }


    /**
     * JeproLinkMapIterator implementation
     */
    protected static class JeproLinkMapIterator<K, V> extends JeproLinkIterator<K, V> implements JeproOrderedMapIterator<K, V>{
        protected JeproLinkMapIterator(final JeproAbstractLinkedMap<K, V> parent){
            super(parent);
        }

        public K next(){
            return super.nextEntry().getKey();
        }

        public K previous(){
            return super.previousEntry().getKey();
        }

        public K getKey(){
            final JeproLinkEntry<K,V> current = currentEntry();
            if(current == null){
                throw new IllegalStateException(JeproLab.getBundle().getString("JEPROLAB_LABEL"));
            }
            return current.getKey();
        }

        public V getValue(final V value){
            final JeproLinkEntry<K, V> current = currentEntry();
            if(current == null){
                throw new IllegalStateException(JeproLab.getBundle().getString("JEPROLAB_LABEL"));
            }
            return current.getValue(value);
        }

        public V setValue(final V value){
            final JeproLinkEntry<K, V> current = currentEntry();
            if(current == null){
                throw new IllegalStateException(JeproLab.getBundle().getString("JEPROLAB_LABEL"));
            }
            return current.setValue(value);
        }
    }

    protected static class JeproEntrySetIterator<K, V> extends JeproLinkIterator<K, V> implements JeproOrderedIterator<Map.Entry<K,V>>, JeproResettableIterrator<Map.Entry<K, V>>{
        protected JeproEntrySetIterator(final JeproAbstractLinkedMap<K, V> parent){
            super(parent);
        }

        public Map.Entry<K, V> next(){
            return super.nextEntry();
        }

        public Map.Entry<K, V> previous(){
            return super.previousEntry();
        }
    }

    protected static class JeproKeySetIterator<K> extends JeproLinkIterator<K, Object> implements JeproOrderedIterator<K>,  JeproResettableIterator<K>{
        protected JeproKeySetIterator(final JeproAbstractLinkedMap<K, ?> parent){
            super((JeproAbstractLinkedMap<K, Object>) parent);
        }

        public K next(){
            return super.nextEntry().getKey();
        }

        public K previous(){
            return super.previousEntry().getKey();
        }
    }

    protected static class JeproValuesIterator<V> extends JeproLinkIterator<Object,  V> implements JeproOrderedIterator<V>, JeproResetableIterator<V>{
        protected JeproValuesIterator(final JeproAbstractLinkedMap<?, V> parent){
            super((JeproAbstractLinkedMap<Object, V>) parent);
        }

        public V next(){
            return super.nextEntry().getValue();
        }

        public V previous(){
            return super.previousEntry().getValue();
        }
    }

    protected static class JeproLinkEntry<K, V> extends JeproHashEntry<K, V>{
        protected  JeproLinkEntry<K, V> before;
        protected  JeproLinkEntry<K, V> after;

        protected JeproLinkEntry(final JeproHashEntry<K, V> next, final int hashCode, final Object key, final V value){
            super(next, hashCode, key, value);
        }
    }

    protected static abstract class JeproLinkIterator<K, V>{
        protected int exceptedModCount;

        protected JeproLinkEntry<K, V> last, next;

        protected final  JeproAbstractLinkedMap<K, V> parent;

        protected JeproLinkIterator(final JeproAbstractLinkedMap<K, V> parent){
            super();
            this.parent = parent;
            this.next = parent.header.after;
            this.exceptedModCount = parent.modCount;
        }

        public boolean hasNext(){
            return next != parent.header;
        }

        public boolean hasPrevious(){
            return next.before != parent.header;
        }

        protected JeproLinkEntry<K, V> nextEntry(){
            if(parent.modCount != exceptedModCount){
                throw new ConcurrentModificationException();
            }

            if(next == parent.header){
                throw new NoSuchElementException(JeproAbstractHashMap.NO_NEXT_ENTRY);
            }
            last = next;
            next = next.after;
            return last;
        }

        protected JeproLinkEntry<K, V> currentEntry(){
            return last;
        }

        public void  remove(){
            if(last == null){
                throw new IllegalStateException(JeproAbstractHashMap.REMOVE_INVALID);
            }
            if(parent.modCount != exceptedModCount){
                throw new ConcurrentModificationException();
            }

            parent.remove(last.getKey());
            last = null;
            exceptedModCount = parent.modCount;
        }

        public void reset(){
            last = null;
            next = parent.header.after;
        }

        @Override
        public String toString(){
            if(last != null){
                return "Iterator[" + last.getKey() + "=" + last.getValue() + "]";
            }
            return "Iterator[]";
        }
    }
}