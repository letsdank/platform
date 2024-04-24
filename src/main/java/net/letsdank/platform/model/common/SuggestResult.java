package net.letsdank.platform.model.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SuggestResult {
    private int total;
    private List<SuggestInfo> suggests;
    private String query;


}
