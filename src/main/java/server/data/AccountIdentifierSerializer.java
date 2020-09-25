package server.data;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;

public class AccountIdentifierSerializer extends StdSerializer<AccountIdentifier> {

    public AccountIdentifierSerializer() {
        this(null);
    }

    public AccountIdentifierSerializer(Class<AccountIdentifier> t) {
        super(t);
    }
    @Override
    public void serialize(AccountIdentifier accountIdentifier, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {

    }
}
