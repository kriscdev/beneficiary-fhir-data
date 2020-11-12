variable "env_config" {
  description = "All high-level info for the whole vpc"
  type        = object({env=string, tags=map(string), vpc_id=string, zone_id=string})
}

variable "role" {
  type        = string
}

variable "layer" {
  description = "app or data"
  type        = string      
}

variable "bcda_acct_num" {
  description = "BCDA"
  type        = string
  default     = "755619740999"
}

variable "bcda_subnet" {
  type = map(list(string))

  default = {
    prod      = ["10.233.20.0/22"]
    prod-sbx  = ["10.233.20.0/22"]
    test      = ["10.233.20.0/22"]
  }
}
