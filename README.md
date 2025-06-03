## Low-carbon-back-end (API REST Java para captura de métricas de GPU e geração de previsões)

Este repositório tem como objetivo oferecer uma solução para a redução da pegada de carbono.

Trata-se de uma API REST desenvolvida em Java com o framework Spring Boot, que atua como servidor central para o recebimento de métricas de uso computacional (GPU, CPU, etc.). Com base nesses dados, a API envia requisições para um serviço externo de inferência, o qual realiza as previsões de uso futuro da GPU. As respostas retornadas pela API de inferência são então armazenadas e disponibilizadas aos clientes por esta API Java.

### Sobre as previsões

As previsões geradas têm como foco exclusivo a métrica de utilização da GPU (`gpu_utilization`), estimando seu comportamento para a próxima hora.

---

Requisitos de execução : 

**Java 21+

Comando para execução : 

```bash
mvn spring-boot:run
```

# API Documentation

## Base URL

```
http://localhost:8080

```

---

## POST `/api/metrics/addMetrics`

Adiciona métricas de GPU e servidor.

### Request Body (application/json)

```json
{
  "timestamp": "2025-06-03T12:00:00Z",
  "gpu_id": "string",
  "gpu_utilization": 0.0,
  "memory_utilization": 0.0,
  "gpu_power_draw": 0.0,
  "gpu_temperature": 0.0,
  "gpu_fan_speed": 0.0,
  "gpu_clock_speed": 0.0,
  "cpu_utilization": 0.0,
  "memory_usage": 0.0,
  "server_power_draw": 0.0,
  "server_temperature": 0.0,
  "disk_usage": 0.0,
  "network_bandwidth": 0.0
}
```

### Response 200

```json
{
  // mesmo corpo do request
}
```

---

## GET `/api/prevision/status`

Verifica o status do serviço de predição.

### Response 200

```text
"OK"
```

---

## GET `/api/prevision/gpu/latest/{gpuCode}`

Retorna a predição mais recente de utilização para o código da GPU fornecido.

### Path Parameter

* `gpuCode` (string) — obrigatório

### Response 200

```json
{
  "id": 0,
  "gpuDevice": {
    "id": 0,
    "code": "string"
  },
  "nextHourAverageUtilization": 0.0,
  "nextHourMaximumUtilization": 0.0,
  "nextHourMinimumUtilization": 0.0,
  "peakProbabilityPercentage": 0.0,
  "confidenceScore": 0.0,
  "riskLevel": "HIGH | MEDIUM | LOW",
  "trend": "INCREASING | DECREASING | STABLE",
  "createdDate": "2025-06-03T12:00:00Z"
}
```

---

## GET `/api/prevision/gpu/id/{gpuId}`

Retorna todas as predições associadas ao ID da GPU.

### Path Parameter

* `gpuId` (integer) — obrigatório

### Response 200

```json
[
  {
    "id": 0,
    "gpuDevice": {
      "id": 0,
      "code": "string"
    },
    "nextHourAverageUtilization": 0.0,
    "nextHourMaximumUtilization": 0.0,
    "nextHourMinimumUtilization": 0.0,
    "peakProbabilityPercentage": 0.0,
    "confidenceScore": 0.0,
    "riskLevel": "HIGH | MEDIUM | LOW",
    "trend": "INCREASING | DECREASING | STABLE",
    "createdDate": "2025-06-03T12:00:00Z"
  }
]
```

---

## GET `/api/prevision/gpu/code/{gpuCode}`

Retorna todas as predições associadas ao código da GPU.

### Path Parameter

* `gpuCode` (string) — obrigatório

### Response 200

```json
[
  {
    "id": 0,
    "gpuDevice": {
      "id": 0,
      "code": "string"
    },
    "nextHourAverageUtilization": 0.0,
    "nextHourMaximumUtilization": 0.0,
    "nextHourMinimumUtilization": 0.0,
    "peakProbabilityPercentage": 0.0,
    "confidenceScore": 0.0,
    "riskLevel": "HIGH | MEDIUM | LOW",
    "trend": "INCREASING | DECREASING | STABLE",
    "createdDate": "2025-06-03T12:00:00Z"
  }
]
```

---


 

  
