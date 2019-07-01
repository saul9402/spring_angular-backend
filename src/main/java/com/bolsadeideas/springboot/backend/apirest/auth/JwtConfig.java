package com.bolsadeideas.springboot.backend.apirest.auth;

public class JwtConfig {

	public static final String LLAVE_SECRETA = "Alguna.Clave.Secreta.Para.Generar.Mi.JWT.Larguisima.Para.Que.Sea.Super.Super.Segura.No.Chingues.Que.Castre.Es.Este.Pedo.xD";
	
	public static final String RSA_PRIVADA = "-----BEGIN RSA PRIVATE KEY-----\r\n" + 
			"MIIEpgIBAAKCAQEAxhQ0RDpLqxKYeV8E6zVln1T6bvjxS9e5LjeDPFyDNsDePJN6\r\n" + 
			"cSKIihXEbZ4MuQAgsD8nOxwmKtaEiV6mBuMzkSkfBX/D3uPJ1rvukvzDpKJBe5jK\r\n" + 
			"axvxFsK0HhWbMRFxSLYpQByzssE2Y70lHjSDtGfOO9aJW7VIneoxwrZngFYV1i8O\r\n" + 
			"rNI+RES0A70L1V/xEwCpxuSR2CqMwiDiQG+qK2frXRzzJg90vTlJm3EaNR33w82Y\r\n" + 
			"hokxnhA7mBmPrauPGHo0ynstvo5vN0XJyTxuBHrNQxzpUO7RbY81CEf1oubof5o8\r\n" + 
			"Li+GCybqCTfaEBxY9SxQ1AwCOOJYvQIVAO1cNQIDAQABAoIBAQCKXMShwCLpLgUq\r\n" + 
			"CvCxqEwDP2XsLKLI+riVCxv2Bx11GsZihBYsfxXndwsOEzAJ7ofm9UZfKOuUUuKj\r\n" + 
			"3mSVPRHhXVsZCUV2JaHxCLe/Hyc+kkSxpGoI5azD3F/a//a3ENsUEoLAEQyJWiO3\r\n" + 
			"CgGqlr5FP5m0/MhYMkq2XEhEHgwwfTaUaHvMXrHBhRR1oYdhMHPDpfT4SqmP5XRt\r\n" + 
			"+w6kxTF0su8A2GrAuu+IYAlxMSX5e1s0y/eu6TtkwVO1vhAXIYOWZHEvdKUwP70N\r\n" + 
			"LP2kD/u9Bc4UinXBZjpkKAc8xjMefsGyIvcQ7rBckUQ4tGck/VwSbEUhc0x12Sn7\r\n" + 
			"6+LLhzahAoGBAO3Ve+pjvwhxvZqscYM7dro45TXBGQGXzF2uyDA0SYa/F23941an\r\n" + 
			"4KrancLSeCH2ZnjfetInponCljKx0FO92Dpm5riiI1U6k/+XK4E9M99XNGZIt/j0\r\n" + 
			"DzW8UfOYYrq6/gE5b5C4d2vRkTN70+c9NAioi1HGoPI4rDboD3WSnvcpAoGBANU1\r\n" + 
			"XZwTBOqKYSxwiD2/RcwMV60u+6DVnWxF3NU72x/5MCFIY+Ux27BT3zsbKWQAPA1a\r\n" + 
			"3nOGTIGsti+rSQc1PgSnmYP/jjEOO1Sa3NauQlMdk+nevQjyRYwk6i7ZoMt2hREe\r\n" + 
			"yJVfJp9r3YWwV9H0/QgwzP9Pq7rYADm0NmDHy9otAoGBALYAcNtliYMEx3veCt/K\r\n" + 
			"WYkYkQ8ZvfEiIRpv7nI1ES80fNGHQLnHrn4Q3jmApr4WuUQ2gN7hyOgTKK2StUHg\r\n" + 
			"PeAzbsxMB43pTeiB3qEAHPoE2eyMKX0yB5Gvk2c+bg9HmamGMoIlJ/4yWVN0pyyG\r\n" + 
			"AZO+px7lSbUS+XbrDymCEnRBAoGBAK/F9UzM6Uqrlv1aMRgm7tYQfuAKUe0A723q\r\n" + 
			"EuvBEyBDAgkOczzrY+D05J8H/5GwF9Iyzh6T49Msnm6iWeyVWnHf8tI+B+4te4B+\r\n" + 
			"w/5BX/DB+8Xdmh8cDXZCoMbLKKNVLNM2e8uZzG+2L8Ud4uj3fvwXr4n1FvFNTrJD\r\n" + 
			"/mi/jzJpAoGBAJMkH2e46xRUxIaO4Y0s7+i6phHHBy8SbFaA5jSaTGegzYPkfme4\r\n" + 
			"J9BBt/0XNYnZ/+xtEcbf57RnVz8sDpVygAKqhyrnfMKHmWJTeEDOCROmuHD02zLq\r\n" + 
			"o50A99PhfMSthF8aai99HPUoNQnTSJT4PVEAeSlexWO0G+phk32l43bb\r\n" + 
			"-----END RSA PRIVATE KEY-----";
	
	public static final String RSA_PUBLICA = "-----BEGIN PUBLIC KEY-----\r\n" + 
			"MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAxhQ0RDpLqxKYeV8E6zVl\r\n" + 
			"n1T6bvjxS9e5LjeDPFyDNsDePJN6cSKIihXEbZ4MuQAgsD8nOxwmKtaEiV6mBuMz\r\n" + 
			"kSkfBX/D3uPJ1rvukvzDpKJBe5jKaxvxFsK0HhWbMRFxSLYpQByzssE2Y70lHjSD\r\n" + 
			"tGfOO9aJW7VIneoxwrZngFYV1i8OrNI+RES0A70L1V/xEwCpxuSR2CqMwiDiQG+q\r\n" + 
			"K2frXRzzJg90vTlJm3EaNR33w82YhokxnhA7mBmPrauPGHo0ynstvo5vN0XJyTxu\r\n" + 
			"BHrNQxzpUO7RbY81CEf1oubof5o8Li+GCybqCTfaEBxY9SxQ1AwCOOJYvQIVAO1c\r\n" + 
			"NQIDAQAB\r\n" + 
			"-----END PUBLIC KEY-----" ;

}
