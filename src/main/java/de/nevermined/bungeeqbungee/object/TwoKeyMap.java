/*
 * This file is part of BungeeQ, licensed under the MIT License.
 *
 *  Copyright (c) 2020 Maurice Hildebrand <webadmin@nevermined.de>
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in all
 *  copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 *  SOFTWARE.
 */

package de.nevermined.bungeeqbungee.object;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class TwoKeyMap<K1, K2, V> {

  private Map<K1, K2> keyMap = new HashMap<>();
  private Map<K2, V> valueMap = new HashMap<>();

  public V getByKey1(K1 k1) {
    K2 k2 = this.keyMap.get(k1);
    V v = k2 != null ? this.valueMap.get(k2) : null;
    return v;
  }

  public V getByKey2(K2 k2) {
    V v = this.valueMap.get(k2);
    return v;
  }

  public Collection<V> values() {
    Collection<V> values = this.valueMap.values();
    return values;
  }

  public Set<K1> getKey1Set() {
    Set<K1> key1Set = this.keyMap.keySet();
    return key1Set;
  }

  public Set<K2> getKeySet2() {
    Set<K2> key2Set = this.valueMap.keySet();
    return key2Set;
  }

  public V put(K1 k1, K2 k2, V v) {
    this.keyMap.put(k1, k2);
    V value = this.valueMap.put(k2, v);
    return value;
  }

  public boolean containsKey1(K1 k1) {
    boolean contains =  this.keyMap.containsKey(k1);
    return contains;
  }

  public boolean containsKey2(K2 k2) {
    boolean contains =  this.valueMap.containsKey(k2);
    return contains;
  }

  public boolean containsValue(V v) {
    boolean contains =  this.valueMap.containsValue(v);
    return contains;
  }

  public V removeByKey1(K1 k1) {
    K2 k2 = this.keyMap.remove(k1);
    V v = k2 != null ? this.valueMap.remove(k2) : null;
    return v;
  }

  public V removeByKey2(K2 k2) {
    K1 k1 = getKey1ByKey2(k2);
    this.keyMap.remove(k1);
    V v = k2 != null ? this.valueMap.remove(k2) : null;

    return v;
  }

  public K2 getKey2ByKey1(K1 k1) {
    K2 k2 = this.keyMap.get(k1);
    return k2;
  }

  public K1 getKey1ByKey2(K2 k2) {
    K1 key1 = null;
    for (K1 k1 : this.getKey1Set()) {
      if (this.keyMap.get(k1).equals(k2)) {
        key1 = k1;
        break;
      }
    }

    return key1;
  }

  public int size() {
    int size = this.valueMap.size();
    return size;
  }

  public boolean isEmpty() {
    boolean empty = this.valueMap.size() != 0;
    return empty;
  }

  public void clear() {
    this.keyMap.clear();
    this.valueMap.clear();
  }
}
