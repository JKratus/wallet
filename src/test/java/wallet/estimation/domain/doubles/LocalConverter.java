package wallet.estimation.domain.doubles;


import org.assertj.core.util.Lists;
import wallet.estimation.domain.ChangeRate;
import wallet.estimation.domain.Converter;
import wallet.estimation.domain.Currency;
import wallet.estimation.domain.Money;
import wallet.stock.domain.Stock;
import wallet.stock.domain.StockType;

import java.util.ArrayList;
import java.util.List;

public class LocalConverter implements Converter {

    private final List<ChangeRate> changeRates;

    private LocalConverter(List<ChangeRate> changeRates) {
        this.changeRates = changeRates;
    }

    public LocalConverter() {
        this.changeRates = new ArrayList<>();
    }

    @Override
    public Money convert(Stock stock, Currency currency) {
        ChangeRate changeRate = find(stock.stockType(), currency);
        return changeRate.apply(stock.value());
    }

    private ChangeRate find(StockType from, Currency to){
        return changeRates.stream().filter(r -> r.sameConversion(from,to)).findAny()
                .orElseThrow(() -> new IllegalArgumentException("There is no rate existing to convert from: "+from+" to: "+to));
    }

    public LocalConverter addRate(ChangeRate changeRate) {
        ArrayList<ChangeRate> changeRates = Lists.newArrayList(this.changeRates);
        changeRates.add(changeRate);
        return new LocalConverter(changeRates);
    }

}
