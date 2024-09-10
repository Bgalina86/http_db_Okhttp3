package ru_inno.constClass;

public class Const {

    public static final int HTTP_CODE_OK = 200;
    //Запрос успешно выполнен. Значение результата «успех» зависит от метода HTTP:
    //
    //GET: Ресурс был получен и передан в теле сообщения.
    //HEAD: Ответ содержит заголовки, но тела сообщения нет.
    //PUT или POST: Ресурс, описывающий результат действия, передан в теле сообщения.
    //TRACE: Тело сообщения содержит сообщение запроса, полученное сервером.
    public static final int HTTP_CODE_CREATE = 201;
    //Запрос выполнен успешно, и в результате был создан новый ресурс. Обычно это ответ,
    // отправляемый на запросы POST или PUT.
    public static final int HTTP_CODE_BAD_REQUEST = 400;
    //Сервер не может или не будет обрабатывать запрос из-за чего-то, что воспринимается как
    // ошибка клиента (например, неправильный синтаксис, формат или маршрутизация запроса).
    public static final int HTTP_CODE_UNAUTHORIZED = 401;
    //неавторизованный
    public static final int HTTP_CODE_FORBIDDEN = 403;
    //Клиент не имеет прав доступа к контенту, то есть он неавторизован,
    // поэтому сервер отказывается предоставить запрошенный ресурс.
    public static final int HTTP_CODE_NOT_FOUND = 404;
    //Сервер не может найти запрошенный ресурс.
    public static final int HTTP_CODE_INTERNAL_SERVER_ERROR = 500;
    public static final int HTTP_CODE_GATEWAY_TIMEOUT = 504;

}
