package logic;

import java.util.ArrayList;
import java.util.List;

public class ResponseBuilder {
  public static List<WordResponse> buildAllPossibleResponses() {
    List<WordResponse> possibleWordResponses = new ArrayList<>();

    LetterResponse[] letterResponses = LetterResponse.values();
    for (LetterResponse a : letterResponses)
      for (LetterResponse b : letterResponses)
        for (LetterResponse c : letterResponses)
          for (LetterResponse d : letterResponses)
            for (LetterResponse e : letterResponses) {
              possibleWordResponses.add(new WordResponse(a,b,c,d,e));
            }
    return possibleWordResponses;
  }
}
