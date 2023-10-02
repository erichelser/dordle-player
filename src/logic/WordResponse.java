package logic;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class WordResponse {
  private final List<LetterResponse> letterResponses;

  public WordResponse(LetterResponse... letterResponses) {
    this.letterResponses = List.of(letterResponses);
  }

  public WordResponse(List<LetterResponse> source) {
    letterResponses = new ArrayList<>();
    letterResponses.addAll(source);
  }

  public WordResponse(String colorGuide) {
    letterResponses = new LinkedList<>();
    for (char letterColor : colorGuide.toCharArray()) {
      letterResponses.add(LetterResponse.of(letterColor));
    }
  }

  public LetterResponse get(int index) {
    return letterResponses.get(index);
  }

  public WordResponse set(int index, LetterResponse newValue) {
    letterResponses.set(index, newValue);
    return this;
  }

  public WordResponse clone() {
    return new WordResponse(letterResponses);
  }

  public int size() {
    return letterResponses.size();
  }

  public String toString() {
    StringBuilder sb = new StringBuilder();
    letterResponses.forEach(lr -> sb.append(lr.getSymbol()));
    return sb.toString();
  }

  @Override
  public boolean equals(Object obj) {
    if(!(obj instanceof WordResponse)){
      return false;
    }
    if(this.size() != ((WordResponse)(obj)).size()){
      return false;
    }
    for(int i=0; i<this.size(); i++){
      if(this.get(i)!=((WordResponse)(obj)).get(i)){
        return false;
      }
    }
    return true;
  }

  @Override
  public int hashCode() {
    return toString().hashCode();
  }
}
