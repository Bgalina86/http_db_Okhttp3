import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * @author [ your email ]
 * @date 2024-08-31 00:03:16 Created by CodeGen .
 */
@RestController
@RequestMapping
public class models {

    @Autowired
    private __model__WriteService __model__WriteService;

    @Autowired
    private __model__ReadService __model__ReadService;

    /**
     * ??
     *
     * @param id
     * @return
     */
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public __model__ find__model__(@PathVariable Long id) {
        return __model__ReadService.findById(id);
    }

    /**
     * ??
     *
     * @param __model__ * @return
     */
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Long create__model__(@RequestBody __model__ __model__) {
        return __model__WriteService.create(__model__);
    }
}