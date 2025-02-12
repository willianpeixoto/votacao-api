package br.com.cooperativa.votacaoapi.controller.v1;

import br.com.cooperativa.votacaoapi.dto.ErroResponseDto;
import br.com.cooperativa.votacaoapi.dto.PautaResponseDto;
import br.com.cooperativa.votacaoapi.dto.PautaRequestDto;
import br.com.cooperativa.votacaoapi.dto.SessaoVotacaoRequestDto;
import br.com.cooperativa.votacaoapi.dto.VotoResponseDto;
import br.com.cooperativa.votacaoapi.dto.VotoRequestDto;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Tag(name = "Pauta", description = "Endpoints para gerenciamento de pautas")
@RequestMapping("/v1/pautas")
public interface PautaApi {

    @Operation(summary = "Cadastrar uma nova pauta")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Pauta cadastrada com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = PautaResponseDto.class),
                            examples = @ExampleObject(value = """
                                {
                                    "idPauta": 1,
                                    "descricaoPauta": "Assunto da pauta",
                                    "mensagem": "Pauta cadastrada com sucesso!"
                                }
                            """)
                    )),
            @ApiResponse(
                    responseCode = "422",
                    description = "Um ou mais atributos com valor inválido na requisição",
                    content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = ErroResponseDto.class),
                        examples = @ExampleObject(value = """
                                {
                                    "timestamp": "2025-02-06T17:50:53.49654855",
                                    "httpStatus": 422,
                                    "mensagem": "O campo 'descricaoPauta' precisa ser informado e não pode ser vazio ou nulo.",
                                    "path": "/v1/pautas"
                                }
                            """)
                    )),
            @ApiResponse(
                    responseCode = "500",
                    description = "Erro interno do servidor",
                    content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = ErroResponseDto.class),
                        examples = @ExampleObject(value = """
                                {
                                    "timestamp": "2025-02-06T17:50:53.49654855",
                                    "httpStatus": 500,
                                    "mensagem": "Ocorreu um erro desconhecido.",
                                    "path": "/v1/pautas"
                                }
                            """)
                    ))
    })

    @PostMapping
    ResponseEntity<PautaResponseDto> cadastrarPauta(@io.swagger.v3.oas.annotations.parameters.RequestBody(
                                                        description = "Dados para criar uma nova pauta",
                                                        required = true,
                                                        content = @Content(
                                                                mediaType = "application/json",
                                                                schema = @Schema(implementation = PautaRequestDto.class),
                                                                examples = @ExampleObject(value = """
                                                                        {
                                                                            "descricaoPauta": "Assunto da pauta"
                                                                        }
                                                                    """)
                                                        ))
                                                    @Valid
                                                    @RequestBody PautaRequestDto pauta);

    @Operation(summary = "Listar todas as pautas")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Lista de pautas retornada",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = PautaResponseDto.class)),
                            examples = @ExampleObject(value = """
                            [
                                {
                                    "idPauta": 1,
                                    "descricaoPauta": "Assunto da pauta",
                                    "inicioSessao": "2025-02-06T19:59:44.964Z",
                                    "fimSessao": "2025-02-06T19:59:44.964Z",
                                    "resultado": "APROVADA"
                                },
                                {
                                    "idPauta": 2,
                                    "descricaoPauta": "Assunto da pauta",
                                    "inicioSessao": "2025-02-06T19:59:44.964Z",
                                    "fimSessao": "2025-02-06T19:59:44.964Z",
                                    "resultado": "REPROVADA"
                                },
                                {
                                    "idPauta": 3,
                                    "descricaoPauta": "Assunto da pauta"
                                }
                            ]
                            """)
                    )),
            @ApiResponse(
                    responseCode = "500",
                    description = "Erro interno do servidor",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErroResponseDto.class),
                            examples = @ExampleObject(value = """
                                    {
                                        "timestamp": "2025-02-06T17:50:53.49654855",
                                        "httpStatus": 500,
                                        "mensagem": "Ocorreu um erro desconhecido.",
                                        "path": "/v1/pautas"
                                    }
                                """)
                    ))
    })
    @GetMapping
    ResponseEntity<List<PautaResponseDto>> listarPautas();

    @Operation(summary = "Abrir sessão de votação em uma pauta através do ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    description = "Sessão de votação aberta com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = PautaResponseDto.class),
                            examples = @ExampleObject(value = """
                                {
                                    "idPauta": 1,
                                    "descricaoPauta": "Assunto da pauta",
                                    "inicioSessao": "2025-02-06T19:59:44.964Z",
                                    "fimSessao": "2025-02-06T20:19:44.964Z"
                                }
                            """)
                    )),
            @ApiResponse(responseCode = "404",
                    description = "Pauta não encontrada, logo a sessão de votação não foi aberta",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErroResponseDto.class),
                            examples = @ExampleObject(value = """
                                {
                                    "timestamp": "2025-02-06T17:50:53.49654855",
                                    "httpStatus": 404,
                                    "mensagem": "Pauta [ID: 1] não encontrada.",
                                    "path": "/v1/pautas/1/abrir-sessao-votacao"
                                }
                            """)
                    )),
            @ApiResponse(responseCode = "409",
                    description = "Não é possível abrir outra sessão de votação para esta pauta",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErroResponseDto.class),
                            examples = @ExampleObject(value = """
                                {
                                    "timestamp": "2025-02-06T17:50:53.49654855",
                                    "httpStatus": 409,
                                    "mensagem": "Já foi aberta uma sessão de votação para esta pauta [ID: 1] anteriormente.",
                                    "path": "/v1/pautas/1/abrir-sessao-votacao"
                                }
                            """)
                    )),
            @ApiResponse(
                    responseCode = "500",
                    description = "Erro interno do servidor",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErroResponseDto.class),
                            examples = @ExampleObject(value = """
                                    {
                                        "timestamp": "2025-02-06T17:50:53.49654855",
                                        "httpStatus": 500,
                                        "mensagem": "Ocorreu um erro desconhecido.",
                                        "path": "/v1/pautas/1/abrir-sessao-votacao"
                                    }
                                """)
                    ))
    })
    @PutMapping("/{id}/abrir-sessao-votacao")
    ResponseEntity<PautaResponseDto> abrirSessaoVotacao(@PathVariable("id") Long id,
                                                        @io.swagger.v3.oas.annotations.parameters.RequestBody(
                                                                description = "Dados para abrir uma sessão de votação (A sessão terá 1min caso 'minutosSessao' não seja informado)",
                                                                content = @Content(
                                                                        mediaType = "application/json",
                                                                        schema = @Schema(implementation = SessaoVotacaoRequestDto.class),
                                                                        examples = @ExampleObject(value = """
                                                                                {
                                                                                    "minutosSessao": 30
                                                                                }
                                                                            """)
                                                                ))
                                                        @RequestBody SessaoVotacaoRequestDto sessaoVotacaoRequestDto);

    @Operation(summary = "Votar em uma pauta através do ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    description = "Voto registrado com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = VotoResponseDto.class),
                            examples = @ExampleObject(value = """
                                {
                                    "idVoto": 1,
                                    "idPauta": 1,
                                    "voto": "Sim",
                                    "mensagem": "Voto registrado com sucesso!"
                                }
                            """)
                    )),
            @ApiResponse(responseCode = "404",
                    description = "Pauta não encontrada, logo a sessão de votação não foi aberta",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErroResponseDto.class),
                            examples = @ExampleObject(value = """
                                {
                                    "timestamp": "2025-02-06T17:50:53.49654855",
                                    "httpStatus": 404,
                                    "mensagem": "Pauta [ID: 1] não encontrada.",
                                    "path": "/v1/pautas/1/votar"
                                }
                            """)
                    )),
            @ApiResponse(responseCode = "403",
                    description = "Não é possível votar pois a sessão de votação não está aberta",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErroResponseDto.class),
                            examples = @ExampleObject(value = """
                                {
                                    "timestamp": "2025-02-06T17:50:53.49654855",
                                    "httpStatus": 403,
                                    "mensagem": "A sessão de votação desta pauta [ID: 1] ainda não foi aberta.",
                                    "path": "/v1/pautas/1/votar"
                                }
                            """)
                    )),
            @ApiResponse(responseCode = "403",
                    description = "Não é possível votar pois a sessão de votação já foi encerrada",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErroResponseDto.class),
                            examples = @ExampleObject(value = """
                                {
                                    "timestamp": "2025-02-06T17:50:53.49654855",
                                    "httpStatus": 403,
                                    "mensagem": "A sessão de votação desta pauta [ID: 1] foi encerrada.",
                                    "path": "/v1/pautas/1/votar"
                                }
                            """)
                    )),
            @ApiResponse(responseCode = "409",
                    description = "Não é possível votar pois o associado já votou nesta pauta",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErroResponseDto.class),
                            examples = @ExampleObject(value = """
                                {
                                    "timestamp": "2025-02-06T17:50:53.49654855",
                                    "httpStatus": 409,
                                    "mensagem": "O associado [ID: 1] já votou nesta pauta [ID 1].",
                                    "path": "/v1/pautas/1/votar"
                                }
                            """)
                    )),
            @ApiResponse(
                    responseCode = "500",
                    description = "Erro interno do servidor",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErroResponseDto.class),
                            examples = @ExampleObject(value = """
                                    {
                                        "timestamp": "2025-02-06T17:50:53.49654855",
                                        "httpStatus": 500,
                                        "mensagem": "Ocorreu um erro desconhecido.",
                                        "path": "/v1/pautas/1/votar"
                                    }
                                """)
                    ))
    })
    @PostMapping("/{id}/votar")
    ResponseEntity<VotoResponseDto> registrarVoto(@PathVariable("id") Long id, @Valid @RequestBody VotoRequestDto votoRequestDto);

    @Operation(summary = "Buscar pauta através do ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    description = "Pauta encontrada com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = PautaResponseDto.class),
                            examples = @ExampleObject(value = """
                                {
                                    "idPauta": 1,
                                    "descricaoPauta": "Assunto que será posto em votação",
                                    "inicioSessao": "2025-02-06T19:59:44.964Z",
                                    "fimSessao": "2025-02-06T19:59:44.964Z",
                                    "resultado": "APROVADA"
                                }
                            """)
                    )),
            @ApiResponse(responseCode = "404",
                    description = "Pauta não encontrada",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErroResponseDto.class),
                            examples = @ExampleObject(value = """
                                {
                                    "timestamp": "2025-02-06T17:50:53.49654855",
                                    "httpStatus": 404,
                                    "mensagem": "Pauta [ID: 1] não encontrada.",
                                    "path": "/v1/pautas/1"
                                }
                            """)
                    )),
            @ApiResponse(
                    responseCode = "500",
                    description = "Erro interno do servidor",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErroResponseDto.class),
                            examples = @ExampleObject(value = """
                                    {
                                        "timestamp": "2025-02-06T17:50:53.49654855",
                                        "httpStatus": 500,
                                        "mensagem": "Ocorreu um erro desconhecido.",
                                        "path": "/v1/pautas/1"
                                    }
                                """)
                    ))
    })
    @GetMapping("/{id}")
    ResponseEntity<PautaResponseDto> buscarPautaPorIdPauta(@PathVariable("id") Long id);

    @Operation(summary = "Contabilizar votação e atualizar resultado na pauta através do ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    description = "Retorna pauta com o resultado da votação",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = PautaResponseDto.class),
                            examples = @ExampleObject(value = """
                                {
                                    "idPauta": 1,
                                    "descricaoPauta": "Assunto que será posto em votação",
                                    "inicioSessao": "2025-02-06T19:59:44.964Z",
                                    "fimSessao": "2025-02-06T19:59:44.964Z",
                                    "resultado": "APROVADA"
                                }
                            """)
                    )),
            @ApiResponse(responseCode = "404",
                    description = "Pauta não encontrada",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErroResponseDto.class),
                            examples = @ExampleObject(value = """
                                {
                                    "timestamp": "2025-02-06T17:50:53.49654855",
                                    "httpStatus": 404,
                                    "mensagem": "Pauta [ID: 1] não encontrada.",
                                    "path": "/v1/pautas/1/consolidar-votacao"
                                }
                            """)
                    )),
            @ApiResponse(responseCode = "403",
                    description = "Não existe sessão de votação para esta pauta",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErroResponseDto.class),
                            examples = @ExampleObject(value = """
                                {
                                    "timestamp": "2025-02-06T17:50:53.49654855",
                                    "httpStatus": 403,
                                    "mensagem": "A sessão de votação desta pauta [ID: 1] ainda não foi aberta.",
                                    "path": "/v1/pautas/1/consolidar-votacao"
                                }
                            """)
                    )),
            @ApiResponse(responseCode = "403",
                    description = "Tempo da sessão de votação ainda não encerrou",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErroResponseDto.class),
                            examples = @ExampleObject(value = """
                                {
                                    "timestamp": "2025-02-06T17:50:53.49654855",
                                    "httpStatus": 403,
                                    "mensagem": "A votação não pode ser consolidada porque a sessão de votação desta pauta [ID: 1] ainda não foi encerrada.",
                                    "path": "/v1/pautas/1/consolidar-votacao"
                                }
                            """)
                    )),
            @ApiResponse(
                    responseCode = "500",
                    description = "Erro interno do servidor",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErroResponseDto.class),
                            examples = @ExampleObject(value = """
                                    {
                                        "timestamp": "2025-02-06T17:50:53.49654855",
                                        "httpStatus": 500,
                                        "mensagem": "Ocorreu um erro desconhecido.",
                                        "path": "/v1/pautas/1/consolidar-votacao"
                                    }
                                """)
                    ))
    })
    @PutMapping("/{id}/consolidar-votacao")
    ResponseEntity<PautaResponseDto> consolidarVotacaoIdPauta(@PathVariable("id") Long id);
}
