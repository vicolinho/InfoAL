package de.uni_leipzig.dbs.formRepository.dataModel.holistic;

import java.util.HashSet;
import java.util.Set;

public class TokenCluster implements Comparable<TokenCluster> {

  private Set<Integer> tokenIds;

  private float aggregateTFIDF;

  private Set<Integer> items;

  private int clusterId;

  private int tokenId;
  static int id = 0;


  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + clusterId;
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    TokenCluster other = (TokenCluster) obj;
    if (clusterId != other.clusterId)
      return false;
    return true;
  }

  public TokenCluster() {
    this.items = new HashSet<Integer>();
    this.tokenIds = new HashSet<Integer>();
  }

  public Set<Integer> getTokenIds() {
    return tokenIds;
  }

  public void setTokenIds(Set<Integer> tokenIds) {
    this.tokenIds = tokenIds;
  }

  public Set<Integer> getItems() {
    return items;
  }

  public void setItems(Set<Integer> items) {
    this.items = items;
  }

  public void addToken(int tid) {
    this.tokenIds.add(tid);
    if (tokenIds.size() == 1) {
      tokenId = tid;
    }

  }

  public int getTokenId() {
    return tokenId;
  }

  public void setTokenId(int tokenId) {
    this.tokenId = tokenId;
  }

  public void addItem(int id) {
    this.items.add(id);
  }

  public float getAggregateTFIDF() {
    return aggregateTFIDF;
  }

  public void setAggregateTFIDF(float aggregateTFIDF) {
    this.aggregateTFIDF = aggregateTFIDF;
  }

  public int getClusterId() {
    return clusterId;
  }

  public void setClusterId(int clusterId) {
    this.clusterId = clusterId;
  }

  public void addItems(Set<Integer> items2) {
    this.items.addAll(items2);

  }

  public void intersectItems(Set<Integer> items) {
    this.items.retainAll(items);
  }


  public int compareTo(TokenCluster o) {
    if (this.tokenIds.size() > o.getTokenIds().size()) {
      return 1;
    } else if (this.tokenIds.size() < o.getTokenIds().size()) {
      return -1;
    }
    return 0;
  }

}
