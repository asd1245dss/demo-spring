package contracts

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    description "should return a list of frauds"

    request {
        url "/frauds"
        method GET()
    }

    response {
        status 201
        body(["John", "Michale"])
    }
}