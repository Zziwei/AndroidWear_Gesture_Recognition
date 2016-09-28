import socket

if __name__ == '__main__':

    # the ip address of the server
    UDP_IP = '192.168.0.106'

    # tge socket port number
    UDP_PORT = 4569

    # initialize the socket and bind it
    sock = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
    sock.bind((UDP_IP, UDP_PORT))

    # receive data from watch
    while True:
        data, addr = sock.recvfrom(1024)
        print data