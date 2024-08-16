package ru.inno.todo.servis;

import java.util.ArrayList;

import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.extension.ExtensionContext;
//import org.junit.jupiter.params.provider.Arguments;
//import org.junit.jupiter.params.provider.ArgumentsProvider;

//public class TitleNewTask implements ArgumentsProvider {
//
//    @Override
//    public Stream<? extends Arguments> provideArguments(ExtensionContext context) throws Exception {
//        List<String> dbMyContent = new ArrayList<>();
//        boolean str1 = dbMyContent.add("{\"title\" : \"test\"}");
//        boolean str2 = dbMyContent.add("{\"title\" : }");
//        boolean str3 = dbMyContent.add("{\"title\" : \" \"}");
//        boolean str4 = dbMyContent.add("{\"title\" : !@#$%^&*(_)/~}");
//        return Stream.of(Arguments.of(str1));
//    }
//}//,Arguments.of(str2),Arguments.of(str3),Arguments.of(str4)
