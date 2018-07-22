package google.main;

class MessageBuilder {

	private StringBuilder message;

	MessageBuilder() {
		this.message = new StringBuilder();
	}

	MessageBuilder withQuery(String query) {
		message.append("Dla zapytania '")
				.append(query)
				.append("' Google");
		return this;
	}

	MessageBuilder withNumberOfResults(Long numberOfResults) {
		if (numberOfResults == null || Long.valueOf(0L).equals(numberOfResults)) {
			message.append(" niczego nie znalazło.");
		} else {
			message.append(" znalazło ok. ")
					.append(String.format("%,d", numberOfResults))
					.append(" wyników.");
		}
		return this;
	}

	String build() {
		return message.toString();
	}

}
