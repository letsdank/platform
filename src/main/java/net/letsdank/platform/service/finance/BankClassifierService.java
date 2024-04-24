package net.letsdank.platform.service.finance;

import lombok.AllArgsConstructor;
import net.letsdank.platform.entity.finance.BankClassifier;
import net.letsdank.platform.model.common.PlatformResult;
import net.letsdank.platform.model.common.SuggestInfo;
import net.letsdank.platform.model.common.SuggestResult;
import net.letsdank.platform.repository.finance.BankClassifierRepository;
import net.letsdank.platform.utils.MessageService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class BankClassifierService {
    private final BankClassifierRepository bankClassifierRepository;

    public PlatformResult createItem() {
        PlatformResult result = new PlatformResult();
        result.addError(MessageService.getMessage("common.bank-classifier.create.interactive-unavailable"), null);
        return result;
    }

    public PlatformResult createGroup() {
        PlatformResult result = new PlatformResult();
        result.addError(MessageService.getMessage("common.bank-classifier.create.interactive-unavailable"), null);
        return result;
    }

    public SuggestResult suggestItem(String query) {
        List<BankClassifier> bankClassifierList = bankClassifierRepository.findItemSuggestions(query);
        return extractSuggests(bankClassifierList, query);
    }

    public SuggestResult suggestGroup(String query) {
        List<BankClassifier> bankClassifierList = bankClassifierRepository.findGroupSuggestions(query);
        return extractSuggests(bankClassifierList, query);
    }

    private SuggestResult extractSuggests(List<BankClassifier> entities, String query) {
        List<SuggestInfo> suggests = new ArrayList<>();
        entities.forEach(bankClassifier -> {
            SuggestInfo suggestInfo = new SuggestInfo();
            suggestInfo.setId(bankClassifier.getId());
            suggestInfo.setName(bankClassifier.getName());
            suggests.add(suggestInfo);
        });

        return new SuggestResult(suggests.size(), suggests, query);
    }
}
