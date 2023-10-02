package logic;

import java.util.ArrayList;
import java.util.List;

public class ResponseBuilder {
  public static List<String> buildAllPossibleResponses() {
    List<String> possibleWordResponses = new ArrayList<>();

    char[] letterResponses = {'-','Y','G'};
    for (char a : letterResponses)
      for (char b : letterResponses)
        for (char c : letterResponses)
          for (char d : letterResponses)
            for (char e : letterResponses) {
              possibleWordResponses.add(""+a+b+c+d+e);
            }
    return possibleWordResponses;
  }
}
