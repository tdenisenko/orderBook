package orderBook;

public class orderBook {
	
	public static final int HISTORIC = 0;
	public static final int SYNTHETIC = 1;
	public static final int BOTH = 2;
	public static int state;
	public static boolean initialized = false;
	public static boolean worstCase = false;
	

	public void step() {
		if (state == HISTORIC)
			setPreviousChange(time);
		else if (state == SYNTHETIC)
			setPreviousSyntheticChange(time);
		else if (state == BOTH) {
			setPreviousChange(time);
			setPreviousSyntheticChange(time);
		}
		// Historic Change
		if ((nextChange != null && nextSyntheticChange == null)
				|| (nextChange != null && (nextChange.getTime() < nextSyntheticChange.getTime()))) {
			setTime(nextChange);
			setTrades();
			state = HISTORIC;
			if (initialized) {
				update();
			}

			else {
				initialize();
				initialized = true;
			}
		}
		// Synthetic Change
		else if ((nextChange == null && nextSyntheticChange != null)
				|| (nextSyntheticChange != null && (nextChange.getTime() > nextSyntheticChange
						.getTime()))) {
			setTime(nextSyntheticChange);
			actual = time;
			trades = newTradeList();
			addSyntheticOrders(syntheticOrders.getOrders(time));
			state = SYNTHETIC;
		}
		// Both historic and synthetic
		else {
			setTime(nextChange);
			setTrades();
			if (!worstCase)
				addSyntheticOrders(syntheticOrders.getOrders(time));
			state = BOTH;
			if (initialized) {
				update();
			} else {
				initialize();
				initialized = true;
			}
			if (worstCase)
				addSyntheticOrders(syntheticOrders.getOrders(time));
		}
		setNextChange();
		setNextSyntheticChange();
	}
	
	private void initialize() {
		previousOrderRowList = orderRowList;
		orderRowList = dataAccess.getOrders(ticker, time);
		
		if (!orderRowList.isEmpty()) {
			actual = orderRowList.getFirst().getTimeStampSource();
			updates++;
			
			for (OrderRow orderRow : orderRowList) {
				if (orderRow.getBidPrice() != null) {
					BigDecimal buyPrice = orderRow.getBidPrice();
					Order buyOrder = new Order(newBuyOrderID(),
												Type.BUY,
												false,
												orderRow.getBidDepth(),
												buyPrice,
												orderRow.getBidNumber(),
												orderRow.getTimeStampSource());
					if (buyOrders.containsKey(buyPrice)) {
						buyOrders.get(buyPrice).add(buyOrder);
					}
					else {
						OrderList buy = new OrderList();
						buy.add(buyOrder);
						buyOrders.put(buyPrice, buy);
					}
				}
				
				if (orderRow.getOfferPrice() != null) {
					BigDecimal sellPrice = orderRow.getOfferPrice();
					Order sellOrder = new Order(newSellOrderID(),
												Type.SELL,
												false,
												orderRow.getOfferDepth
												sellPrice,
												orderRow.getOfferNumber(),
												orderRow.getTimeStampSource());
					if (sellOrders.containsKey(sellPrice)) {
						sellOrders.get(sellPrice).add(sellOrder);
					}
					else {
						OrderList sell = new OrderList();
						sell.add(sellOrder);
						sellOrders.put(sellPrice.sell):
					}
				}
			}
		}
	}
	
	/*
	 * 
	 *
	 */
}
